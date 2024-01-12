package com.recombee.apiclientkotlin

import java.net.URLEncoder
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.lang.reflect.Type
import java.lang.reflect.ParameterizedType

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request as OkHttp3Request
import okhttp3.RequestBody.Companion.toRequestBody

import com.recombee.apiclientkotlin.util.NetworkClient
import com.recombee.apiclientkotlin.util.Region
import com.recombee.apiclientkotlin.requests.*
import com.recombee.apiclientkotlin.bindings.*
import com.recombee.apiclientkotlin.exceptions.*
import java.io.InterruptedIOException
import java.net.SocketTimeoutException

/**
 * Client for interacting with Recombee API.
 *
 * @property databaseId ID of your database in Recombee.
 * @property publicToken Associated public token for authentication.
 * @property useHttpsByDefault Indicates if HTTPS should be used as the default protocol (otherwise HTTP is used).
 * @property region The region of the Recombee cluster where the database is located
 * @property baseUri Custom URI of the recommendation API.
 * @property port Custom port for the API requests.
 */
public class RecombeeClient(
    private val databaseId: String,
    publicToken: String,
    private val useHttpsByDefault: Boolean = true,
    region: Region? = null,
    baseUri: String? = null,
    private val port: Int? = null
) {
    private val publicTokenBytes: ByteArray = publicToken.toByteArray(StandardCharsets.UTF_8)
    private val hostUri: String = getHostUri(baseUri, region)

    private fun getRegionalBaseUri(region: Region): String {
        return when (region) {
            Region.ApSe -> "client-rapi-ap-se.recombee.com"
            Region.CaEast -> "client-rapi-ca-east.recombee.com"
            Region.EuWest -> "client-rapi-eu-west.recombee.com"
            Region.UsWest -> "client-rapi-us-west.recombee.com"
        }
    }

    private fun getHostUri(baseUri: String?, region: Region?): String {
        val envHostUri = System.getenv("RAPI_URI")
        var hostUri = envHostUri ?: baseUri

        if (region != null) {
            if (hostUri != null) {
                throw IllegalArgumentException("baseUri and region cannot be specified at the same time")
            }
            hostUri = getRegionalBaseUri(region)
        }

        return hostUri ?: "client-rapi-eu-west.recombee.com"
    }

    private fun createJsonRequestBody(parameters: Map<String, Any>): RequestBody {
        val json = Gson().toJson(parameters)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        return json.toRequestBody(mediaType)
    }

    private fun getCustomIOException(request: Request<*>, e: IOException): ApiException {
        val exception = when (e) {
            is SocketTimeoutException -> ApiTimeoutException(request, e)
            is InterruptedIOException -> ApiTimeoutException(request, e)
            else -> ApiIOException(request=request, originalException = e)
        }
        return exception
    }

    private fun createHttpClient(request: Request<*>): OkHttpClient {
        val requestClient = NetworkClient.client.newBuilder()
            .callTimeout(request.timeout, TimeUnit.MILLISECONDS)
            .build()
        return requestClient
    }

    public fun executeRequest(
        request: Request<*>,
        onResponse: ((String) -> Unit)? = null,
        onFailure: ((ApiException) -> Unit)? = null
    ) {
        val okHttpRequest = createOkHttpRequest(request)

        createHttpClient(request).newCall(okHttpRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure?.invoke(getCustomIOException(request, e))
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        val exception = ResponseException(
                            request = request,
                            statusCode = response.code,
                            response.body?.string() ?: ""
                        )
                        onFailure?.invoke(exception)
                    } else {
                        onResponse?.invoke(response.body?.string() ?: "")
                    }
                }
            }
        })
    }

    public suspend fun executeRequestAsync(
        request: Request<*>
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val okHttpRequest = createOkHttpRequest(request)

                val requestClient = createHttpClient(request)

                val response = requestClient.newCall(okHttpRequest).execute()

                if (!response.isSuccessful) {
                    Result.failure(
                        ResponseException(
                            request = request,
                            statusCode = response.code,
                            response.body?.string() ?: ""
                        )
                    )
                } else {
                    response.use {
                        Result.success(response.body?.string() ?: "")
                    }
                }
            } catch (e: IOException) {
                Result.failure(getCustomIOException(request, e))
            }
        }
    }

    private fun createOkHttpRequest(request: Request<*>): OkHttp3Request {
        return OkHttp3Request.Builder()
            .url(processRequestUri(request))
            .post(createJsonRequestBody(request.bodyParameters))
            .header("User-Agent", "recombee-kotlin-api-client/4.1.0")
            .build()
    }

    private fun processRequestUri(request: Request<*>): String {
        val scheme = if (request.ensureHttps || useHttpsByDefault) "https" else "http"
        val urlBuilder = HttpUrl.Builder()
            .scheme(scheme)
            .host(hostUri)
            .addPathSegment(databaseId)
            .addEncodedPathSegments(request.path.trimStart('/'))

        port?.let { urlBuilder.port(it) }

        request.queryParameters.forEach { (key, value) ->
            urlBuilder.addQueryParameter(key, URLEncoder.encode(value.toString(), "UTF-8"))
        }

        appendHmacParameters(urlBuilder)

        return urlBuilder.build().toString()
    }

    private fun buildUrlWithoutSchemeAndBase(urlBuilder: HttpUrl.Builder): String {
        val temporaryUrl = urlBuilder.build()

        val path = temporaryUrl.encodedPath
        val query = temporaryUrl.encodedQuery

        val pathWithQuery = if (query != null) {
            "$path?$query"
        } else {
            path
        }
        return pathWithQuery
    }

    private fun appendHmacParameters(urlBuilder: HttpUrl.Builder) {
        val timestamp = unixTimestampNow().toString()
        urlBuilder.addQueryParameter("frontend_timestamp", timestamp)

        val urlToBeSigned = buildUrlWithoutSchemeAndBase(urlBuilder)
        Mac.getInstance("HmacSHA1").apply {
            init(SecretKeySpec(publicTokenBytes, "HmacSHA1"))
            val hmacBytes = doFinal(urlToBeSigned.toByteArray(StandardCharsets.UTF_8))
            val hmacRes = hmacBytes.joinToString("") { String.format("%02x", it) }
            urlBuilder.addQueryParameter("frontend_sign", hmacRes)
        }
    }

    private fun unixTimestampNow(): Int {
        return (System.currentTimeMillis() / 1000L).toInt()
    }


    public inline fun <reified ResponseType> processResponse(
        body: String,
        request: Request<*>
    ): ResponseType {
        return when {
            ResponseType::class == String::class -> body as ResponseType
            request is Batch -> parseBatchResponse(request, body) as ResponseType
            else -> {
                val type: Type = object : TypeToken<ResponseType>() {}.type
                Gson().fromJson<ResponseType>(body, type)
            }
        }
    }

    /**
     * Asynchronously sends a request and processes the response.
     *
     * This function uses Kotlin coroutines for asynchronous execution. It sends a request, processes the response,
     * and wraps the result in a `Result` object which encapsulates either a successful result or an exception.
     *
     * @param request The request to be sent. The request type also determines the type of the response.
     * @return A `Result` object containing the response of type `ResponseType` if successful, or an exception if not.
     * @param ResponseType The expected type of the response. This is inferred from the request type.
     * @throws ApiException if there is an error processing the response.
     */
    public suspend inline fun <reified ResponseType> sendAsync(
        request: Request<ResponseType>
    ): Result<ResponseType> {
        return withContext(Dispatchers.IO) {
            val result = executeRequestAsync(request)
            result.mapCatching { body ->
                processResponse<ResponseType>(body, request)
            }
        }
    }

    /**
     * Sends a request and processes the response, providing callbacks for success and failure.
     *
     * This function sends a request and processes the response. On successful response processing, the `onResponse`
     * callback is invoked with the response. In case of an error, the `onFailure` callback is invoked with the exception.
     *
     * @param request The request to be sent. The request type also determines the type of the response.
     * @param onResponse A callback to be invoked with the response if the request is successful.
     * @param onFailure A callback to be invoked with an `ApiException` if the request fails.
     * @param ResponseType The expected type of the response. This is inferred from the request type.
     */
    public inline fun <reified ResponseType> send(
        request: Request<ResponseType>,
        noinline onResponse: ((ResponseType) -> Unit)? = null,
        noinline onFailure: ((ApiException) -> Unit)? = null
    ) {
        return executeRequest(request, { body ->
            try {
                val response = processResponse<ResponseType>(body, request)
                onResponse?.invoke(response)
            } catch (e: ApiException) {
                onFailure?.invoke(e)
            }
        }, onFailure)
    }

    private data class ResponseItem(
        val code: Int,
        val json: JsonElement
    )

    public fun parseBatchResponse(request: Batch, jsonResponse: String): List<BatchResponse> {
        val intermediateType: Type = object : TypeToken<List<ResponseItem>>() {}.type
        val responseList = Gson().fromJson<List<ResponseItem>>(jsonResponse, intermediateType)
        val result = mutableListOf<BatchResponse>()

        request.requests.forEachIndexed { index, subReq ->
            val subRes = responseList[index]

            if (subRes.code < 200 || subRes.code > 299) {
                result.add(BatchResponse(subRes.code, ResponseException(subReq, subRes.code, subRes.json.toString())))
            } else {
                val responseType = (subReq.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
                val typeToken = TypeToken.get(responseType)
                val parsedResponse = Gson().fromJson<Any>(subRes.json, typeToken.type)
                result.add(BatchResponse(subRes.code, parsedResponse))
            }
        }
        return result
    }
}
