package com.example.cafesolace.assets

import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// A suspend function to fetch JSON content from a given URL asynchronously
suspend fun fetchJsonFromUrl(url: String): String? = withContext(Dispatchers.IO) {
    try {
        // Create an OkHttpClient instance to perform network requests
        val client = OkHttpClient()

        // Build an HTTP GET request with the provided URL
        val request = Request.Builder().url(url).build()

        // Execute the request synchronously in the IO dispatcher thread pool
        val response = client.newCall(request).execute()

        // Check if the HTTP response status code indicates success (2xx)
        if (response.isSuccessful) {
            // Return the response body as a String if available
            response.body?.string()
        } else {
            // Return null if response is not successful
            null
        }
    } catch (e: Exception) {
        // Print the stack trace for debugging if an exception occurs
        e.printStackTrace()
        // Return null in case of any exception (network failure, etc.)
        null
    }
}
