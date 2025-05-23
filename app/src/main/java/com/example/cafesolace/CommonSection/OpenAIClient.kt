package com.example.cafesolace.CommonSection

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun callOpenAIChatAPI(userMessage: String, apiKey: String, callback: (String?) -> Unit) {
    val client = OkHttpClient()

    val jsonBody = JSONObject().apply {
        put("model", "gpt-3.5-turbo")
        put("messages", JSONArray().apply {
            put(JSONObject().apply {
                put("role", "user")
                put("content", userMessage)
            })
        })
    }

    val requestBody = RequestBody.create(
        "application/json".toMediaTypeOrNull(),
        jsonBody.toString()
    )

    val request = Request.Builder()
        .url("https://api.openai.com/v1/chat/completions")
        .header("Authorization", "Bearer $apiKey")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            callback(null)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val message = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    callback(message)
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
    })
}
