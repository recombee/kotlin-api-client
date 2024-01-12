/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Add Detail View
 * @param userId User who viewed the item
 * @param itemId Viewed item
 * @param timestamp UTC timestamp of the view as ISO8601-1 pattern or UTC epoch time. The default value is the current time.
 * @param duration Duration of the view
 * @param cascadeCreate Sets whether the given user/item should be created if not present in the database.
 * @param recommId If this detail view is based on a recommendation request, `recommId` is the id of the clicked recommendation.
 * @param additionalData A dictionary of additional data for the interaction.
 */
public class AddDetailView (
    public val userId: String,
    public val itemId: String,
    public val timestamp: Instant? = null,
    public val duration: Long? = null,
    public val cascadeCreate: Boolean? = true,
    public val recommId: String? = null,
    public val additionalData: Map<String, Any>? = null
): Request<String>(1000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  "/detailviews/"

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
                "itemId" to itemId
            )
            timestamp?.let { parameters["timestamp"] = it.toString()}
            duration?.let { parameters["duration"] = it}
            cascadeCreate?.let { parameters["cascadeCreate"] = it}
            recommId?.let { parameters["recommId"] = it}
            additionalData?.let { parameters["additionalData"] = it}
            return parameters
        }

}
