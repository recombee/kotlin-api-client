/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Merge Users
 * @param targetUserId ID of the target user.
 * @param sourceUserId ID of the source user.
 * @param cascadeCreate Sets whether the user *targetUserId* should be created if not present in the database.
 */
public class MergeUsers (
    public val targetUserId: String,
    public val sourceUserId: String,
    public val cascadeCreate: Boolean? = true
): Request<String>(10000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  String.format("/users/$targetUserId/merge/$sourceUserId")

    /**
     * The query parameters for the request.
     *
     * A map containing the names and values of the query parameters.
    */
    override val queryParameters: Map<String, Any>
       get() {
            val parameters = mutableMapOf<String, Any>(
    
            )
            cascadeCreate?.let { parameters["cascadeCreate"] = it}
            return parameters
        }

    /**
     * The body parameters for the request.
     *
     * A map containing the names and values of the body parameters.
    */
    override val bodyParameters: Map<String, Any>
        get() = emptyMap()

}
