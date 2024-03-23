package com.example.kotlin17

import okhttp3.OkHttpClient
import okhttp3.Request

object NetworkService {
    private val client = OkHttpClient()

    fun fetchPostsJson(): String? {
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/posts")
            .build()
        client.newCall(request).execute().use { response ->
            return if (response.isSuccessful) response.body?.string() else null
        }
    }
}
