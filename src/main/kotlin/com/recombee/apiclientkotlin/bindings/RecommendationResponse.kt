/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.bindings
/**
 * RecommendationResponse
 * @param recommId Id of the recommendation request
 * @param recomms Obtained recommendations
 * @param numberNextRecommsCalls How many times *Recommend Next Items* have been called for this `recommId`
 * @param abGroup Name of AB-testing group to which the request belongs if there is a custom AB-testing running.
 */
public data class RecommendationResponse(
    public val recommId: String,
    public val recomms: List<Recommendation>,
    public val numberNextRecommsCalls: Long? = null,
    public val abGroup: String? = null): RecombeeBinding() {
}

