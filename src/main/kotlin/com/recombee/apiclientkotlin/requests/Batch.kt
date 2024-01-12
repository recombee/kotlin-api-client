package com.recombee.apiclientkotlin.requests
import com.recombee.apiclientkotlin.bindings.*

/**
 * Represents a batch request combining multiple [Request] objects.
 * This allows for sending multiple requests in a single API call.
 *
 * @property requests A list of [Request] objects to be included in the batch.
 * @property distinctRecomms A flag to indicate if all recommended items should be distinct across multiple recommendation requests in the batch. Optional.
 */
public class Batch (
    public val requests: List<Request<*>>,
    public val distinctRecomms: Boolean? = null
): Request< List<BatchResponse> >(1 + requests.sumOf { it.timeout }) {

    /**
     * Ensures that HTTPS is used for the batch request.
     * Always returns true as HTTPS is required for batch requests.
     */
    override val ensureHttps: Boolean
        get() = true
    
    /**
     * Path for the batch endpoint.
     */
    override val path: String
        get() = "/batch/"

    /**
     * The query parameters for the request.
     * A map containing the names and values of the query parameters.
     */
    override val queryParameters: Map<String, Any>
        get() = emptyMap()

    /**
     * The body parameters for the request.
     * This includes all individual requests and the distinct recommendations flag.
     * A map containing the names and values of the body parameters.
     */

    /**
     * The body parameters for the request.
     * This includes all individual requests and the distinct recommendations flag.
     * A map containing the names and values of the body parameters.
     */
    override val bodyParameters: Map<String, Any>
        get() {
            val requestMaps = requests.map { requestToBatchMap(it) }

            val result = mutableMapOf<String, Any>()
            result["requests"] = requestMaps
            distinctRecomms?.let { result["distinctRecomms"] = it }
            return result
        }

    /**
     * Converts an individual [Request] into a map format suitable for the batch request.
     *
     * @param req The [Request] to be converted.
     * @return A map representing the individual request in the batch.
     */
    protected fun requestToBatchMap(req: Request<*>): Map<String, Any> {
        val bm = mutableMapOf<String, Any>()
        bm["path"] = req.path
        bm["method"] = "post"

        val params = req.queryParameters.toMutableMap()
        params.putAll(req.bodyParameters)
        if (params.isNotEmpty()) bm["params"] = params
        return bm
    }
}