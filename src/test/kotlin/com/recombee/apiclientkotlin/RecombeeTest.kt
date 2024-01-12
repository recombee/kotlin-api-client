package com.recombee.apiclientkotlin

import com.recombee.apiclientkotlin.util.Region

open class RecombeeTest {

    fun getClient(): RecombeeClient {
        val client = RecombeeClient(
            databaseId = "tst-public-key",
            publicToken = "dIuCAwTeXn87m24HYE6uAIwSVzgEkTrQflrYlDBeIoeTIrhG1FVqj1v0h6u3nNSu",
            region = Region.EuWest
        )
        return client
    }
}