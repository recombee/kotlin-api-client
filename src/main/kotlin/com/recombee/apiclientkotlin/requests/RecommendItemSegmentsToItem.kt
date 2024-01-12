/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Recommend Item Segments to Item
 * @param itemId ID of the item for which the recommendations are to be generated.
 * @param targetUserId ID of the user who will see the recommendations.

Specifying the *targetUserId* is beneficial because:

* It makes the recommendations personalized
* Allows the calculation of Actions and Conversions
  in the graphical user interface,
  as Recombee can pair the user who got recommendations
  and who afterward viewed/purchased an item.

If you insist on not specifying the user, pass `null`
(`None`, `nil`, `NULL` etc., depending on the language) to *targetUserId*.
Do not create some special dummy user for getting recommendations,
as it could mislead the recommendation models,
and result in wrong recommendations.

For anonymous/unregistered users, it is possible to use, for example, their session ID.

 * @param count Number of item segments to be recommended (N for the top-N recommendation).

 * @param scenario Scenario defines a particular application of recommendations. It can be, for example, "homepage", "cart", or "emailing".

You can set various settings to the [scenario](https://docs.recombee.com/scenarios.html) in the [Admin UI](https://admin.recombee.com). You can also see the performance of each scenario in the Admin UI separately, so you can check how well each application performs.

The AI that optimizes models to get the best results may optimize different scenarios separately or even use different models in each of the scenarios.

 * @param cascadeCreate If the user does not exist in the database, returns a list of non-personalized recommendations and creates the user in the database. This allows, for example, rotations in the following recommendations for that user, as the user will be already known to the system.

 * @param filter Boolean-returning [ReQL](https://docs.recombee.com/reql.html) expression which allows you to filter recommended segments based on the `segmentationId`.

 * @param booster Number-returning [ReQL](https://docs.recombee.com/reql.html) expression which allows you to boost recommendation rate of some segments based on the `segmentationId`.

 * @param logic Logic specifies the particular behavior of the recommendation models. You can pick tailored logic for your domain and use case.
See [this section](https://docs.recombee.com/recommendation_logics.html) for a list of available logics and other details.

The difference between `logic` and `scenario` is that `logic` specifies mainly behavior, while `scenario` specifies the place where recommendations are shown to the users.

Logic can also be set to a [scenario](https://docs.recombee.com/scenarios.html) in the [Admin UI](https://admin.recombee.com).

 * @param expertSettings Dictionary of custom options.

 * @param returnAbGroup If there is a custom AB-testing running, return the name of the group to which the request belongs.

 */
public class RecommendItemSegmentsToItem (
    public val itemId: String,
    public val targetUserId: String,
    public val count: Long,
    public val scenario: String? = null,
    public val cascadeCreate: Boolean? = true,
    public val filter: String? = null,
    public val booster: String? = null,
    public val logic: Logic? = null,
    public val expertSettings: Map<String, Any>? = null,
    public val returnAbGroup: Boolean? = null
): Request<RecommendationResponse>(3000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  String.format("/recomms/items/$itemId/item-segments/")

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
                "targetUserId" to targetUserId,
                "count" to count
            )
            scenario?.let { parameters["scenario"] = it}
            cascadeCreate?.let { parameters["cascadeCreate"] = it}
            filter?.let { parameters["filter"] = it}
            booster?.let { parameters["booster"] = it}
            logic?.let { parameters["logic"] = it}
            expertSettings?.let { parameters["expertSettings"] = it}
            returnAbGroup?.let { parameters["returnAbGroup"] = it}
            return parameters
        }

}
