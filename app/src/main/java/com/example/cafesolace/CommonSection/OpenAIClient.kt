package com.example.cafesolace.CommonSection

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

fun callOpenAIChatAPI(userMessage: String, apiKey: String, callback: (String?) -> Unit) {
    val client = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

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
        "application/json".toMediaType(),
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
            response.use {
                if (it.isSuccessful) {
                    val responseBody = it.body?.string()
                    if (responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            val message = jsonResponse.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                            callback(message)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            callback(null)
                        }
                    } else {
                        callback(null)
                    }
                } else {
                    println("OpenAI API Error: ${it.code} - ${it.body?.string()}")
                    callback(null)
                }
            }
        }
    })
}
