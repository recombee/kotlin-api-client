package com.recombee.apiclientkotlin

import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

import com.recombee.apiclientkotlin.RecombeeTest
import com.recombee.apiclientkotlin.requests.*
import com.recombee.apiclientkotlin.bindings.*


class TestBatch: RecombeeTest() {

    @Test
    fun callbackTest() {
        // Initialize the real RecombeeClient
        val client = getClient()

        // Create a Batch request
        val request = Batch(listOf(AddBookmark("user-1", "item-1"), SearchItems("user-1", "computer", 5), RecommendItemsToItem("item-1", "user-1", 5, filter="true or false")))

        // CountDownLatch for waiting for the response
        val latch = CountDownLatch(1)

        // Define a flag to track if the request was successful
        var requestSuccessful = false

        // Call the send method
        client.send(request,
            onResponse = { response ->
                // Handle successful response
                requestSuccessful = true

                
                latch.countDown()
            },
            onFailure = { exception ->
                // Handle failure
                latch.countDown()
                fail("Request failed with exception: ${exception.message}")
            }
        )

        // Wait for the response (with a timeout to avoid indefinitely waiting)
        val responseReceived = latch.await(30, TimeUnit.SECONDS)

        // Assert that the response was received and the request was successful
        assertTrue(responseReceived, "The response was not received within the timeout period.")
        assertTrue(requestSuccessful, "The request did not complete successfully.")
    }

    @Test
    fun coroutineTest() = runBlocking {
        // Initialize the real RecombeeClient
        val client = getClient()

        // Create a Batch request
        val request = Batch(listOf(AddBookmark("user-1", "item-1"), SearchItems("user-1", "computer", 5), RecommendItemsToItem("item-1", "user-1", 5, filter="true or false")))

        // Call the sendAsync method and wait for the result
        val result = client.sendAsync(request)

        // Process the result
        result.onSuccess { response ->
            // Handle successful response
            
        }.onFailure { exception ->
            // Handle failure
            fail("Request failed with exception: ${exception.message}")
        }

        // Additional assertions can be added if necessary
        assertTrue(result.isSuccess, "The request did not complete successfully.")
    }

}