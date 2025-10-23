/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.bindings
/**
 * CompositeRecommendationResponse
 * @param recommId Id of the composite recommendation request
 * @param source Parameters of the source stage
 * @param recomms Obtained recommendations
 * @param numberNextRecommsCalls How many times *Recommend Next Items* have been called for this `recommId`
 */
public data class CompositeRecommendationResponse(
    public val recommId: String,
    public val source: Recommendation,
    public val recomms: List<Recommendation>,
    public val numberNextRecommsCalls: Long? = null): RecombeeBinding() {
}

