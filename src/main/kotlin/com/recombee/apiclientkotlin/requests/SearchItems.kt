/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Search Items
 * @param userId ID of the user for whom personalized search will be performed.
 * @param searchQuery Search query provided by the user. It is used for the full-text search.
 * @param count Number of items to be returned (N for the top-N results).
 * @param scenario Scenario defines a particular search field in your user interface.

You can set various settings to the [scenario](https://docs.recombee.com/scenarios.html) in the [Admin UI](https://admin.recombee.com). You can also see the performance of each scenario in the Admin UI separately, so you can check how well each field performs.

The AI that optimizes models to get the best results may optimize different scenarios separately, or even use different models in each of the scenarios.

 * @param cascadeCreate If the user does not exist in the database, returns a list of non-personalized search results and creates the user in the database. This allows, for example, rotations in the following recommendations for that user, as the user will be already known to the system.
 * @param returnProperties With `returnProperties=true`, property values of the recommended items are returned along with their IDs in a JSON dictionary. The acquired property values can be used to easily display the recommended items to the user. 

Example response:
```
  {
    "recommId": "ce52ada4-e4d9-4885-943c-407db2dee837",
    "recomms": 
      [
        {
          "id": "tv-178",
          "values": {
            "description": "4K TV with 3D feature",
            "categories":   ["Electronics", "Televisions"],
            "price": 342,
            "url": "myshop.com/tv-178"
          }
        },
        {
          "id": "mixer-42",
          "values": {
            "description": "Stainless Steel Mixer",
            "categories":   ["Home & Kitchen"],
            "price": 39,
            "url": "myshop.com/mixer-42"
          }
        }
      ],
    "numberNextRecommsCalls": 0
  }
```

 * @param includedProperties Allows specifying which properties should be returned when `returnProperties=true` is set. The properties are given as a comma-separated list.

Example response for `includedProperties=description,price`:
```
  {
    "recommId": "a86ee8d5-cd8e-46d1-886c-8b3771d0520b",
    "recomms":
      [
        {
          "id": "tv-178",
          "values": {
            "description": "4K TV with 3D feature",
            "price": 342
          }
        },
        {
          "id": "mixer-42",
          "values": {
            "description": "Stainless Steel Mixer",
            "price": 39
          }
        }
      ],
    "numberNextRecommsCalls": 0
  }
```

 * @param filter Boolean-returning [ReQL](https://docs.recombee.com/reql.html) expression, which allows you to filter recommended items based on the values of their attributes.

Filters can also be assigned to a [scenario](https://docs.recombee.com/scenarios.html) in the [Admin UI](https://admin.recombee.com).

 * @param booster Number-returning [ReQL](https://docs.recombee.com/reql.html) expression, which allows you to boost the recommendation rate of some items based on the values of their attributes.

Boosters can also be assigned to a [scenario](https://docs.recombee.com/scenarios.html) in the [Admin UI](https://admin.recombee.com).

 * @param logic Logic specifies the particular behavior of the recommendation models. You can pick tailored logic for your domain and use case.
See [this section](https://docs.recombee.com/recommendation_logics.html) for a list of available logics and other details.

The difference between `logic` and `scenario` is that `logic` specifies mainly behavior, while `scenario` specifies the place where recommendations are shown to the users.

Logic can also be set to a [scenario](https://docs.recombee.com/scenarios.html) in the [Admin UI](https://admin.recombee.com).

 * @param expertSettings Dictionary of custom options.

 * @param returnAbGroup If there is a custom AB-testing running, return the name of the group to which the request belongs.

 */
public class SearchItems (
    public val userId: String,
    public val searchQuery: String,
    public val count: Long,
    public val scenario: String? = null,
    public val cascadeCreate: Boolean? = true,
    public val returnProperties: Boolean? = null,
    public val includedProperties: List<String>? = null,
    public val filter: String? = null,
    public val booster: String? = null,
    public val logic: Logic? = null,
    public val expertSettings: Map<String, Any>? = null,
    public val returnAbGroup: Boolean? = null
): Request<SearchResponse>(3000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  String.format("/search/users/$userId/items/")

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
                "searchQuery" to searchQuery,
                "count" to count
            )
            scenario?.let { parameters["scenario"] = it}
            cascadeCreate?.let { parameters["cascadeCreate"] = it}
            returnProperties?.let { parameters["returnProperties"] = it}
            includedProperties?.let { parameters["includedProperties"] = it}
            filter?.let { parameters["filter"] = it}
            booster?.let { parameters["booster"] = it}
            logic?.let { parameters["logic"] = it}
            expertSettings?.let { parameters["expertSettings"] = it}
            returnAbGroup?.let { parameters["returnAbGroup"] = it}
            return parameters
        }

}
