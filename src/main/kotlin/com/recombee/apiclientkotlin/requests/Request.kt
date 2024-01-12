package com.recombee.apiclientkotlin.requests

/**
 * Base class for all the requests.
 */
public abstract class Request<ResponseType>(public var timeout: Long) {

    /**
     * Determines whether HTTPS must be used for this request.
     * True if HTTPS must be chosen over HTTP, false otherwise.
     */
    open public val ensureHttps: Boolean
        get() = false

    /**
     * A string representing the path part of the URI.
     */
    abstract public val path: String

    /**
     * The query parameters for the request.
     * A map containing the names and values of the query parameters.
     */
    abstract public val queryParameters: Map<String, Any>

    /**
     * The body parameters for the request.
     * A map containing the names and values of the body parameters.
     */
    abstract public val bodyParameters: Map<String, Any>
}