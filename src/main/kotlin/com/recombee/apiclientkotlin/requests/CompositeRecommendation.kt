/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Composite Recommendation
 * @param scenario Scenario defines a particular application of recommendations. It can be, for example, "homepage", "cart", or "emailing".

You can set various settings to the [scenario](https://docs.recombee.com/scenarios) in the [Admin UI](https://admin.recombee.com). You can also see the performance of each scenario in the Admin UI separately, so you can check how well each application performs.

The AI that optimizes models to get the best results may optimize different scenarios separately or even use different models in each of the scenarios.

 * @param count Number of items to be recommended (N for the top-N recommendation).

 * @param itemId ID of the item for which the recommendations are to be generated.

 * @param userId ID of the user for which the recommendations are to be generated.

 * @param logic Logic specifies the particular behavior of the recommendation models. You can pick tailored logic for your domain and use case.
See [this section](https://docs.recombee.com/recommendation_logics) for a list of available logics and other details.

The difference between `logic` and `scenario` is that `logic` specifies mainly behavior, while `scenario` specifies the place where recommendations are shown to the users.

Logic can also be set to a [scenario](https://docs.recombee.com/scenarios) in the [Admin UI](https://admin.recombee.com).

 * @param segmentId ID of the segment from `contextSegmentationId` for which the recommendations are to be generated.

 * @param cascadeCreate If the entity for the source recommendation does not exist in the database, returns a list of non-personalized recommendations and creates the user in the database. This allows, for example, rotations in the following recommendations for that entity, as the entity will be already known to the system.

 * @param sourceSettings Parameters applied for recommending the *Source* stage. The accepted parameters correspond with the recommendation sub-endpoint used to recommend the *Source*.

 * @param resultSettings Parameters applied for recommending the *Result* stage. The accepted parameters correspond with the recommendation sub-endpoint used to recommend the *Result*.

 * @param expertSettings Dictionary of custom options.

 */
public class CompositeRecommendation (
    public val scenario: String,
    public val count: Long,
    public val itemId: String? = null,
    public val userId: String? = null,
    public val logic: Logic? = null,
    public val segmentId: String? = null,
    public val cascadeCreate: Boolean? = true,
    public val sourceSettings: CompositeRecommendationStageParameters? = null,
    public val resultSettings: CompositeRecommendationStageParameters? = null,
    public val expertSettings: Map<String, Any>? = null
): Request<CompositeRecommendationResponse>(3000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  "/recomms/composite/"

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
                "scenario" to scenario,
                "count" to count
            )
            itemId?.let { parameters["itemId"] = it}
            userId?.let { parameters["userId"] = it}
            logic?.let { parameters["logic"] = it}
            segmentId?.let { parameters["segmentId"] = it}
            cascadeCreate?.let { parameters["cascadeCreate"] = it}
            sourceSettings?.let { parameters["sourceSettings"] = it}
            resultSettings?.let { parameters["resultSettings"] = it}
            expertSettings?.let { parameters["expertSettings"] = it}
            return parameters
        }

}
