package com.recombee.apiclientkotlin.bindings

/**
 * Represents a recommended Item / User / Item Segment
 *
 * @property id The identifier of the recommended entity.
 */
public data class Recommendation(
    public val id: String,
    private val values: Map<String, Any>? = null
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
}