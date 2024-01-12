package com.recombee.apiclientkotlin.util

import okhttp3.OkHttpClient

/**
 * Singleton object that provides a shared instance of [OkHttpClient].
 *
 * This shared instance is lazily initialized and can be used throughout the library
 * to make HTTP requests.
 */
public object NetworkClient {
    /**
     * A shared [OkHttpClient] instance for making HTTP requests.
     * 
     * This instance is initialized the first time it is accessed.
     */
    public val client: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
}