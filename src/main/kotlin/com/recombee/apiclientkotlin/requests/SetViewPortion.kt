/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Set View Portion
 * @param userId User who viewed a portion of the item
 * @param itemId Viewed item
 * @param portion Viewed portion of the item (number between 0.0 (viewed nothing) and 1.0 (viewed full item) ). It should be the actual viewed part of the item, no matter the seeking. For example, if the user seeked immediately to half of the item and then viewed 10% of the item, the `portion` should still be `0.1`.
 * @param sessionId ID of the session in which the user viewed the item. Default is `null` (`None`, `nil`, `NULL` etc., depending on the language).
 * @param timestamp UTC timestamp of the rating as ISO8601-1 pattern or UTC epoch time. The default value is the current time.
 * @param cascadeCreate Sets whether the given user/item should be created if not present in the database.
 * @param recommId If this view portion is based on a recommendation request, `recommId` is the id of the clicked recommendation.
 * @param additionalData A dictionary of additional data for the interaction.
 */
public class SetViewPortion (
    public val userId: String,
    public val itemId: String,
    public val portion: Double,
    public val sessionId: String? = null,
    public val timestamp: Instant? = null,
    public val cascadeCreate: Boolean? = true,
    public val recommId: String? = null,
    public val additionalData: Map<String, Any>? = null
): Request<String>(1000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  "/viewportions/"

    /**
     * The query parameters for the request.
     *
     * A map containing the names and values of the query parameters.
    */
    override val queryParameters: Map<String, Any>
        get() = emptyMap()

    /**
     * The body parameters for the request.
     *
     * A map containing the names and values of the body parameters.
    */
    override val bodyParameters: Map<String, Any>
       get() {
           val parameters = mutableMapOf<String, Any>(
                "userId" to userId,
                "itemId" to itemId,
                "portion" to portion
            )
            sessionId?.let { parameters["sessionId"] = it}
            timestamp?.let { parameters["timestamp"] = it.toString()}
            cascadeCreate?.let { parameters["cascadeCreate"] = it}
            recommId?.let { parameters["recommId"] = it}
            additionalData?.let { parameters["additionalData"] = it}
            return parameters
        }

}
