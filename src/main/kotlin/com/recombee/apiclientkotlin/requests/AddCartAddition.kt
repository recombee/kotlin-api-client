/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Add Cart Addition
 * @param userId User who added the item to the cart
 * @param itemId Item added to the cart
 * @param timestamp UTC timestamp of the cart addition as ISO8601-1 pattern or UTC epoch time. The default value is the current time.
 * @param cascadeCreate Sets whether the given user/item should be created if not present in the database.
 * @param amount Amount (number) added to cart. The default is 1. For example, if `user-x` adds two `item-y` during a single order (session...), the `amount` should equal 2.
 * @param price Price of the added item. If `amount` is greater than 1, the sum of prices of all the items should be given.
 * @param recommId If this cart addition is based on a recommendation request, `recommId` is the id of the clicked recommendation.
 * @param additionalData A dictionary of additional data for the interaction.
 */
public class AddCartAddition (
    public val userId: String,
    public val itemId: String,
    public val timestamp: Instant? = null,
    public val cascadeCreate: Boolean? = true,
    public val amount: Double? = null,
    public val price: Double? = null,
    public val recommId: String? = null,
    public val additionalData: Map<String, Any>? = null
): Request<String>(1000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  "/cartadditions/"

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
            cascadeCreate?.let { parameters["cascadeCreate"] = it}
            amount?.let { parameters["amount"] = it}
            price?.let { parameters["price"] = it}
            recommId?.let { parameters["recommId"] = it}
            additionalData?.let { parameters["additionalData"] = it}
            return parameters
        }

}
