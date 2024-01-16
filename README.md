# Recombee API Client

A Kotlin client (SDK) for easy use of the [Recombee](https://www.recombee.com/) recommendation API in Android applications.

If you don't have an account at Recombee yet, you can create a free account [here](https://www.recombee.com/).

Documentation of the API can be found at [docs.recombee.com](https://docs.recombee.com/).

## Installation

The client is available in the [Maven Central Repository](https://mvnrepository.com/artifact/com.recombee/apiclientkotlin/), so you just need to add the following entry to your gradle.build file:

```gradle
repositories {
   mavenCentral()
}

dependencies {
   implementation "com.recombee.apiclientkotlin:4.1.0"
}
```

## How to use

This library allows you to request recommendations and send interactions between users and items (views, bookmarks, purchases ...) to Recombee. It uses the **public token** for authentication.

It is intentionally not possible to change the item catalog (properties of items) with the public token, so you should use one of the following ways to send it to Recombee:

 - Use one of the server-side SDKs (Node.js, PHP, Java...). The synchronization can done for example by a peridodically run script. See [this section](https://docs.recombee.com/gettingstarted.html#managing-item-catalog) for more details.
 - Set a catalog feed at [Recombee web admin](https://admin.recombee.com/).

### Sending interactions

```kotlin
import com.recombee.apiclientkotlin.RecombeeClient
import com.recombee.apiclientkotlin.util.Region
import com.recombee.apiclientkotlin.exceptions.ApiException
import com.recombee.apiclientkotlin.requests.*


// Initialize client with name of your database and PUBLIC token
val client = RecombeeClient(
    databaseId = "id-of-your-db",
    publicToken = "...db-public-token...",
    region = Region.UsWest
)

// Interactions take the ID of the user and the ID of the item
client.send(AddBookmark(userId = "user-13434", itemId = "item-256"))
client.send(AddCartAddition(userId = "user-4395", itemId = "item-129"))
client.send(AddDetailView(userId = "user-9318", itemId = "item-108"))
client.send(AddPurchase(userId = "user-7499", itemId = "item-750"))
client.send(AddRating(userId = "user-3967", itemId = "item-365", rating = 0.5))
client.send(SetViewPortion(userId = "user-4289", itemId = "item-487", portion = 0.3))
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
    scenario = "homepage-for-you"
)

client.send(request,
    { recommendationResponse: RecommendationResponse ->
        for (recommendedItem in recommendationResponse.recomms) {
            println("ID: ${recommendedItem.id}")
        }
    },
    { exception: ApiException ->
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
    scenario = "homepage-for-you"
)

val result = client.sendAsync(request)

result.onSuccess { recommendationResponse: RecommendationResponse ->
    for (recommendedItem in recommendationResponse.recomms) {
        println("ID: ${recommendedItem.id}")
    }
}.onFailure { exception -> // ApiException
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
    { searchResponse: SearchResponse ->
        for (recommendedItem in searchResponse.recomms) {
            println("ID: ${recommendedItem.id} Values: ${recommendedItem.getValues()}")
        }
    },
    { exception: ApiException ->
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

result.onSuccess { searchResponse: SearchResponse ->
    for (recommendedItem in searchResponse.recomms) {
        println("ID: ${recommendedItem.id} Values: ${recommendedItem.getValues()}")
    }
}.onFailure { exception -> // ApiException
    println("Exception: $exception")
    // use fallback ...
}
```

### Recommend Next Items

Recombee can return items that shall be shown to a user as next recommendations when the user e.g. scrolls the page down (infinite scroll) or goes to the next page. See [Recommend next items](https://docs.recombee.com/api.html#recommend-next-items) for more info.

```kotlin
client.sendAsync(RecommendItemsToUser("user-1", 5))
    .onSuccess { firstResponse: RecommendationResponse ->

        client.sendAsync(RecommendNextItems(firstResponse.recommId, 5))
            .onSuccess { secondResponse: RecommendationResponse ->
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
