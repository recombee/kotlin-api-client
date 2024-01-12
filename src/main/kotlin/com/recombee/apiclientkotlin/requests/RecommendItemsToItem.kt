/*
 This file is auto-generated, do not edit
*/

package com.recombee.apiclientkotlin.requests
import java.time.Instant
import com.recombee.apiclientkotlin.requests.Request
import com.recombee.apiclientkotlin.bindings.*

/**
 * Recommend Items to Item
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

 * @param count Number of items to be recommended (N for the top-N recommendation).
 * @param scenario Scenario defines a particular application of recommendations. It can be, for example, "homepage", "cart", or "emailing".

You can set various settings to the [scenario](https://docs.recombee.com/scenarios.html) in the [Admin UI](https://admin.recombee.com). You can also see the performance of each scenario in the Admin UI separately, so you can check how well each application performs.

The AI that optimizes models to get the best results may optimize different scenarios separately or even use different models in each of the scenarios.

 * @param cascadeCreate If an item of the given *itemId* or user of the given *targetUserId* doesn't exist in the database, it creates the missing entity/entities and returns some (non-personalized) recommendations. This allows, for example, rotations in the following recommendations for the user of the given *targetUserId*, as the user will be already known to the system.
 * @param returnProperties With `returnProperties=true`, property values of the recommended items are returned along with their IDs in a JSON dictionary. The acquired property values can be used to easily display the recommended items to the user. 

Example response:
```
  {
    "recommId": "0c6189e7-dc1a-429a-b613-192696309361",
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
    "recommId": "6842c725-a79f-4537-a02c-f34d668a3f80",
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

 * @param userImpact **Expert option** If *targetUserId* parameter is present, the recommendations are biased towards the given user. Using *userImpact*, you may control this bias. For an extreme case of `userImpact=0.0`, the interactions made by the user are not taken into account at all (with the exception of history-based blacklisting), for `userImpact=1.0`, you'll get a user-based recommendation. The default value is `0`.

 * @param diversity **Expert option** Real number from [0.0, 1.0], which determines how mutually dissimilar the recommended items should be. The default value is 0.0, i.e., no diversification. Value 1.0 means maximal diversification.

 * @param minRelevance **Expert option** If the *targetUserId* is provided:  Specifies the threshold of how relevant must the recommended items be to the user. Possible values one of: "low", "medium", "high". The default value is "low", meaning that the system attempts to recommend a number of items equal to *count* at any cost. If there is not enough data (such as interactions or item properties), this may even lead to bestseller-based recommendations being appended to reach the full *count*. This behavior may be suppressed by using "medium" or "high" values. In such case, the system only recommends items of at least the requested relevance and may return less than *count* items when there is not enough data to fulfill it.

 * @param rotationRate **Expert option** If the *targetUserId* is provided: If your users browse the system in real-time, it may easily happen that you wish to offer them recommendations multiple times. Here comes the question: how much should the recommendations change? Should they remain the same, or should they rotate? Recombee API allows you to control this per request in a backward fashion. You may penalize an item for being recommended in the near past. For the specific user, `rotationRate=1` means maximal rotation, `rotationRate=0` means absolutely no rotation. You may also use, for example, `rotationRate=0.2` for only slight rotation of recommended items.

 * @param rotationTime **Expert option** If the *targetUserId* is provided: Taking *rotationRate* into account, specifies how long it takes for an item to recover from the penalization. For example, `rotationTime=7200.0` means that items recommended less than 2 hours ago are penalized.

 * @param expertSettings Dictionary of custom options.

 * @param returnAbGroup If there is a custom AB-testing running, return the name of the group to which the request belongs.

 */
public class RecommendItemsToItem (
    public val itemId: String,
    public val targetUserId: String,
    public val count: Long,
    public val scenario: String? = null,
    public val cascadeCreate: Boolean? = true,
    public val returnProperties: Boolean? = null,
    public val includedProperties: List<String>? = null,
    public val filter: String? = null,
    public val booster: String? = null,
    public val logic: Logic? = null,
    public val userImpact: Double? = null,
    public val diversity: Double? = null,
    public val minRelevance: String? = null,
    public val rotationRate: Double? = null,
    public val rotationTime: Double? = null,
    public val expertSettings: Map<String, Any>? = null,
    public val returnAbGroup: Boolean? = null
): Request<RecommendationResponse>(3000) {

    /**
     * A string representing the path part of the URI.
    */
    override val path: String 
        get() =  String.format("/recomms/items/$itemId/items/")

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
            returnProperties?.let { parameters["returnProperties"] = it}
            includedProperties?.let { parameters["includedProperties"] = it}
            filter?.let { parameters["filter"] = it}
            booster?.let { parameters["booster"] = it}
            logic?.let { parameters["logic"] = it}
            userImpact?.let { parameters["userImpact"] = it}
            diversity?.let { parameters["diversity"] = it}
            minRelevance?.let { parameters["minRelevance"] = it}
            rotationRate?.let { parameters["rotationRate"] = it}
            rotationTime?.let { parameters["rotationTime"] = it}
            expertSettings?.let { parameters["expertSettings"] = it}
            returnAbGroup?.let { parameters["returnAbGroup"] = it}
            return parameters
        }

}
