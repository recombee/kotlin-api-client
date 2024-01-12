package com.recombee.apiclientkotlin.bindings

import com.recombee.apiclientkotlin.exceptions.ApiException

/**
 * Represents the response for a single request sent within a [Batch].
 *
 * @property statusCode The HTTP status code of the response.
 * @param response The response content. This varies depending on the request type: for instance, 
 *                 `RecommendItemsToUser` responses are `RecommendationResponse`, 
 *                 `SearchItems` responses are `SearchResponse`, etc.
 */
public data class BatchResponse(
    public val statusCode: Int,
    private val response: Any
) {
    /**
     * True if the request within the Batch was successful.
     */
    public val successful: Boolean
        get() = statusCode in 200..299

    /**
     * Retrieves the response content if the request was successful.
     * 
     * @throws ApiException if the request was not successful.
     * @return The response content. If the request was RecommendItemsToUser then the response is a RecommendationResponse, 
     *                               if it was SearchItems it is SearchResponse, etc.
     */
    @Throws(ApiException::class)
    public fun getResponse(): Any {
        if (!successful) throw response as ApiException
        return response
    }
}
