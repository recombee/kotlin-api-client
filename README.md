<div align="center">
  <img
    src="https://raw.githubusercontent.com/recombee/.github/refs/heads/main/assets/mark.svg"
    width="64px"
    align="center"
    alt="Recombee"
  />
  <br />
  <h1>Recombee API Client</h1>
</div>

<p align="center">
<a href="https://mvnrepository.com/artifact/com.recombee/apiclientkotlin" rel="nofollow"><img src="https://img.shields.io/maven-central/v/com.recombee/apiclientkotlin" alt="Version"></a>
<a href="https://opensource.org/licenses/MIT" rel="nofollow"><img src="https://img.shields.io/github/license/recombee/kotlin-api-client" alt="License"></a>
</p>

<div align="center">
  <a href="https://docs.recombee.com/kotlin_client">Documentation</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="https://github.com/recombee/kotlin-api-client/issues/new">Issues</a>
  <span>&nbsp;&nbsp;•&nbsp;&nbsp;</span>
  <a href="mailto:support@recombee.com">Support</a>
  <br />
</div>

## ✨ Features

- Thin Kotlin wrapper around the Recombee API
- Supported endpoints: [Interactions](https://docs.recombee.com/api#user-item-interactions), [Recommendations](https://docs.recombee.com/api#recommendations) & [Search](https://docs.recombee.com/api#search)
- Made for Android apps and Compose Multiplatform

## 🚀 Getting Started

Add the dependency into your `build.gradle.kts`:

```kotlin
implementation("com.recombee:apiclientkotlin:6.0.0")
```

### 📚 Version Catalogs

If you're using [version catalogs](https://developer.android.com/build/migrate-to-catalogs), first add the dependency into your `libs.versions.toml`:

```toml
[versions]
recombee = "6.0.0"

[libraries]
recombee = { group = "com.recombee", name = "apiclientkotlin", version.ref = "recombee" }
```

Then reference it in your `build.gradle.kts`:

```kotlin
implementation(libs.recombee)
```

### 🏗️ Example

You can send user-item interactions and receive recommendations as follows:

```kotlin
// Initialize the API client with the ID of your database and the associated PUBLIC token
val client =
    RecombeeClient(
        databaseId = "database-id",
        publicToken = "...db-public-token...",
        region = Region.UsWest // the region of your database
    )

// Send interactions
client.send(
    AddDetailView(
        userId = "user-4395",
        itemId = "item-129",
        recommId = "23eaa09b-0e24-4487-ba9c-8e255feb01bb",
    )
)

// Request recommendations
client.send(
    // Get 10 items for "user-x" using the "homepage-top-for-you" scenario from the Admin UI
    RecommendItemsToUser(
        userId = "user-x",
        count = 10,
        scenario = "homepage-top-for-you",
        returnProperties = true,
        includedProperties = listOf("title")
    ),
    { response: RecommendationResponse ->
        // `recommId` needs to be sent with interactions based on recommendations
        println("recommId: ${response.recommId}")

        // The `recomms` object contains the `id` (and `values` if `returnProperties` is true)
        for (item in response.recomms) {
            println("ID: ${item.id}, Title: ${item.getValues()["title"]}")
        }
    },
    { exception: ApiException ->
        println("Exception: $exception")
        // Ideally, you should provide a fallback if an error occurs...
    }
)
```

Coroutine support is also available, simply replace `send` with `sendAsync`:

```kotlin
suspend fun getItems(): List<Item> {
    // Get 10 items for "user-x" using the "homepage-top-for-you" scenario from the Admin UI
    val result =
        client.sendAsync(
            RecommendItemsToUser(
                userId = "user-x",
                count = 10,
                scenario = "homepage-top-for-you",
                returnProperties = true,
                includedProperties = listOf("title", "images")
            )
        )

    // Ideally, you should provide a fallback if an error occurs
    if (result.isFailure) {
        return listOf()
    }

    val data = result.getOrNull() ?: return listOf()

    // `recommId` needs to be sent with interactions based on recommendations
    println("recommId: ${data.recommId}")

    // Map the recommendations to your own internal data type
    return data.recomms.map { item ->
        Item(
            id = item.id,
            title = item.getValues()["title"] as? String ?: "",
            images = item.getValues()["images"] as? List<String> ?: listOf(),
            recommId = data.recommId
        )
    }
}
```

> [!TIP]
> We also published a simple [example Android app](https://github.com/recombee/android-demo) to help you with the integration. Feel free to use it as a reference.
>
> ![Android Demo app](https://raw.githubusercontent.com/recombee/android-demo/refs/heads/main/images/screenshots.png)

## 📝 Documentation

Discover the full [Kotlin API Client documentation](https://docs.recombee.com/kotlin_client) for comprehensive guides and examples.

For a complete breakdown of all endpoints and their responses, check out our [API Reference](https://docs.recombee.com/api).

## 🤝 Contributing

We welcome all contributions—whether it’s fixing a bug, improving documentation, or suggesting a new feature.

To contribute, simply fork the repository, make your changes, and submit a pull request. Be sure to provide a clear description of your changes.

Thanks for helping make this project better!

## 🔧 Troubleshooting

Are you having issues? We recommend checking [our documentation](https://docs.recombee.com/kotlin_client) to see if it contains a possible solution.

If you want to reach out, you can either [open a GitHub issue](https://github.com/recombee/kotlin-api-client/issues/new) or send an email to support@recombee.com.

## 📄 License

The Recombee Kotlin API Client is provided under the [MIT License](https://opensource.org/licenses/MIT).
