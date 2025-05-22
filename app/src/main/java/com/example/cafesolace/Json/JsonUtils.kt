package com.example.cafesolace.Json

import android.content.Context
import org.json.JSONObject
import java.io.InputStream

fun loadJsonFromAssets(context: Context, fileName: String): JSONObject {
    val inputStream: InputStream = context.assets.open(fileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    val json = String(buffer, Charsets.UTF_8)
    return JSONObject(json)
}