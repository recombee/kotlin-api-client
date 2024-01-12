package com.recombee.apiclientkotlin.exceptions
import com.recombee.apiclientkotlin.requests.Request
import java.io.IOException

/**
 * Represents an API exception that occurs due to an IOException.
 *
 * This class provides a specific exception type for handling IOExceptions
 * that occur during API requests, encapsulating the original exception
 * and the request that caused it. It extends the ApiException class to
 * allow for consistent handling of all API-related exceptions.
 *
 * @property request The API request that caused the exception.
 * @property cause The original IOException that occurred.
 */
public open class ApiIOException(
    public val request: Request<*>,
    message: String? = null,
    public val originalException: IOException? = null
) : ApiException(
    message ?: originalException?.message ?: "Request unexpectedly failed"
)