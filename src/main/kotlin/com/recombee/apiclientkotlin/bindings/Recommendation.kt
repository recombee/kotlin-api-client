package com.recombee.apiclientkotlin.bindings

/**
 * Represents a recommended Item / User / Item Segment
 *
 * @property id The identifier of the recommended entity.
 * @property reqlEvaluations The results of ReQL expressions provided in the request.
 */
public data class Recommendation(
    public val id: String,
    private val values: Map<String, Any>? = null,
    private val reqlEvaluations: Map<String, Any>? = null,
) : RecombeeBinding() {

    /**
     * Retrieves the values of properties associated with the recommended entity.
     *
     * @return A map of property names and their values.
     * @throws IllegalStateException if the recommendation was not meant to return values 
     *         (this can be controlled using the `returnProperties` parameter in the request).
     */
    public fun getValues(): Map<String, Any> {
        return values ?: throw IllegalStateException("The request was not meant to return values (use returnProperties parameter)")
    }

    /**
     * Retrieves the evaluations of reqlExpressions for the recommended entity.
     *
     * @return A map of expression names and their evaluations.
     * @throws IllegalStateException if the recommendation contains no reqlEvaluations
     *         (this means none were sent in the `reqlExpressions` parameter of the request).
     */
    public fun getReqlEvaluations(): Map<String, Any> {
        return reqlEvaluations ?: throw IllegalStateException("The recommendation contains no ReQL evaluations (reqlExpressions must be provided in the request)")
    }
}
