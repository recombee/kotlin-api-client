package com.recombee.apiclientkotlin.exceptions

import com.recombee.apiclientkotlin.requests.Request

/**
 * Exception thrown when a request did not succeed (i.e., did not return HTTP status 200 OK or 201 Created).
 *
 * @property request The request which failed.
 * @property statusCode The HTTP status code obtained from the failed request.
 * @param message The detailed message of the exception.
 */
public class ResponseException(
    public val request: Request<*>,
    public val statusCode: Int, 
    message: String
) : ApiException(message)