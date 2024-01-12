package com.recombee.apiclientkotlin.exceptions

import com.recombee.apiclientkotlin.requests.Request
import java.io.IOException

/**
 * Exception thrown when a request is not processed within the timeout.
 *
 * @property request The request that timed out.
 * @property cause The original [IOException] that was thrown due to the timeout, if any.
 */
public class ApiTimeoutException(
    request: Request<*>,
    originalException: IOException
) : ApiIOException(
    request = request, message = "The request was not processed within ${request.timeout} ms", originalException=originalException)