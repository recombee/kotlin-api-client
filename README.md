# Recombee API Client

A Kotlin client (SDK) for easy use of the [Recombee](https://www.recombee.com/) recommendation API in Android applications.

If you don't have an account at Recombee yet, you can create a free account [here](https://www.recombee.com/).

Documentation of the API can be found at [docs.recombee.com](https://docs.recombee.com/).

## Installation

The client will available in the Maven Central Repository in the upcoming days.

## How to use

This library allows you to request recommendations and send interactions between users and items (views, bookmarks, purchases ...) to Recombee. It uses the **public token** for authentication.

It is intentionally not possible to change the item catalog (properties of items) with the public token, so you should use one of the following ways to send it to Recombee:

 - Use one of the server-side SDKs (Node.js, PHP, Java...). The synchronization can done for example by a peridodically run script. See [this section](https://docs.recombee.com/gettingstarted.html#managing-item-catalog) for more details.
 - Set a catalog feed at [Recombee web admin](https://admin.recombee.com/).

### Sending interactions

```kotlin
import com.recombee.apiclientkotlin.RecombeeClient
import com.recombee.apiclientkotlin.util.Region
import com.recombee.apiclientkotlin.requests.*


// Initialize client with name of your database and PUBLIC token
val client = RecombeeClient(
    databaseId = "id-of-your-db",
    publicToken = "...db-public-token...",
    region = Region.UsWest
)

// Interactions take the ID of the user and the ID of the item
client.send(AddBookmark("user-13434", "item-256"))
client.send(AddCartAddition("user-4395", "item-129"))
client.send(AddDetailView("user-9318", "item-108"))
client.send(AddPurchase("user-7499", "item-750"))
client.send(AddRating("user-3967", "item-365", 0.5))
client.send(SetViewPortion("user-4289", "item-487", 0.3))
```

### Requesting recommendations

You can [recommend items to user](https://docs.recombee.com/api.html#recommend-items-to-user), [recommend items to item](https://docs.recombee.com/api.html#recommend-items-to-item) or even [recommend Item Segments](https://docs.recombee.com/api#recommend-item-segments-to-user) such as categories, genres or artists.

It is possible to use callbacks (`send` method) or coroutines (`sendAsync` method).

#### Callbacks

There are two callbacks (both are optional):
- `onResponse`: Callback function invoked in case of successful response.

- `onFailure`: Callback function invoked with an *ApiException* in case of a failure.

```kotlin
val request = RecommendItemsToUser(
    userId = "user-x", 
    count = 10, 
    scenario = "homepage-for-you", 
    returnProperties = true
)

client.send(request,
    { recommendationResponse ->  // response of type RecommendationResponse
        for (recommendedItem in recommendationResponse.recomms) {
            println("ID: ${recommendedItem.id} Values: ${recommendedItem.getValues()}")
        }
    },
    { exception ->
        println("Exception: $exception")
        // use fallback ...
    }
)
```


#### Coroutines

```kotlin
// Assuming this is inside a CoroutineScope

val request = RecommendItemsToUser(
    userId = "user-x", 
    count = 10, 
    scenario = "homepage-for-you", 
    returnProperties = true
)

val result = client.sendAsync(request)

result.onSuccess { recommendationResponse ->
    for (recommendedItem in recommendationResponse.recomms) {
        println("ID: ${recommendedItem.id} Values: ${recommendedItem.getValues()}")
    }
}.onFailure { exception ->
    println("Exception: $exception")
    // use fallback ...
}

```

### Personalized search

[Personalized full-text search](https://docs.recombee.com/api.html#search-items) is requested in the same way as recommendations.

#### Callbacks

```kotlin
val request = SearchItems(
    userId = "user-x",
    searchQuery = "..user's search query",
    count = 10,
    scenario = "search",
    returnProperties = true
)

client.send(request,
    { searchResponse ->  // response of type SearchResponse
        for (recommendedItem in searchResponse.recomms) {
            println("ID: ${recommendedItem.id} Values: ${recommendedItem.getValues()}")
        }
    },
    { exception ->
        println("Exception: $exception")
        // use fallback ...
    }
)
```


#### Coroutines

```kotlin
// Assuming this is inside a CoroutineScope

val request = SearchItems(
    userId = "user-x",
    searchQuery = "..user's search query",
    count = 10,
    scenario = "search",
    returnProperties = true
)

val result = client.sendAsync(request)

result.onSuccess { searchResponse ->
    for (recommendedItem in searchResponse.recomms) {
        println("ID: ${recommendedItem.id} Values: ${recommendedItem.getValues()}")
    }
}.onFailure { exception ->
    println("Exception: $exception")
    // use fallback ...
}
```

### Recommend Next Items

Recombee can return items that shall be shown to a user as next recommendations when the user e.g. scrolls the page down (infinite scroll) or goes to the next page. See [Recommend next items](https://docs.recombee.com/api.html#recommend-next-items) for more info.

```kotlin
client.sendAsync(RecommendItemsToUser("user-1", 5))
    .onSuccess { firstResponse ->

        client.sendAsync(RecommendNextItems(firstResponse.recommId, 5))
            .onSuccess { secondResponse ->
                // Show next recommendations
            }
    }
```

### Optional parameters

Recommendation requests accept various optional parameters (see [the docs](https://docs.recombee.com/api.html#recommendations)). Following example shows some of them:

```kotlin
val request = RecommendItemsToUser(
    userId = "user-13434",
    count = 5,
    scenario = "homepage-for-you", // Label particular usage
    returnProperties = true, // Return properties of the recommended items
    includedProperties = listOf("title", "img_url", "url", "price"), // Properties to be included in the response
    filter = "'title' != null AND 'availability' == \"in stock\"" // Filter condition
)
```

## Exception handling

Following types of exceptions can be produced:
- *ResponseException*: Recombee API returned an error code (e.g. due to passing an invalid value to a parameter)
- *ApiIOException*: Request did not succeed
  - In case of an timeout a subtype *ApiTimeoutException* is produced

*ApiException* is the base class of both *ResponseException* and *ApiIOException*.
