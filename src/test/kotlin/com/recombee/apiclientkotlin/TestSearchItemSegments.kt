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


class TestSearchItemSegments: RecombeeTest() {

    @Test
    fun callbackTest() {
        // Initialize the real RecombeeClient
        val client = getClient()

        // Create a SearchItemSegments request
        val request = SearchItemSegments("user-1", "computer", 5, scenario = "s-is")

        // CountDownLatch for waiting for the response
        val latch = CountDownLatch(1)

        // Define a flag to track if the request was successful
        var requestSuccessful = false

        // Call the send method
        client.send(request,
            onResponse = { response ->
                // Handle successful response
                requestSuccessful = true

                assertEquals(response.recomms.size, 5)
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

        // Create a SearchItemSegments request
        val request = SearchItemSegments("user-1", "computer", 5, scenario = "s-is")

        // Call the sendAsync method and wait for the result
        val result = client.sendAsync(request)

        // Process the result
        result.onSuccess { response ->
            // Handle successful response
            assertEquals(response.recomms.size, 5)
        }.onFailure { exception ->
            // Handle failure
            fail("Request failed with exception: ${exception.message}")
        }

        // Additional assertions can be added if necessary
        assertTrue(result.isSuccess, "The request did not complete successfully.")
    }

}
