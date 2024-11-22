package com.capstone.bindetective.utils

import android.content.Context
import android.widget.Toast
import com.capstone.bindetective.model.User
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

// Function to create the user via API
fun createUser(context: Context, idToken: String, user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val apiUrl = "https://bindetective-1-2-476908432279.asia-southeast2.run.app/users" // Update with your API URL

    // Create JSON data for the POST body
    val jsonData = JSONObject().apply {
        put("userName", user.userName)
        put("dateOfBirth", user.dateOfBirth)
    }

    // Create the request body
    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val requestBody = jsonData.toString().toRequestBody(mediaType)

    // Create the HTTP client
    val client = OkHttpClient()

    // Build the request
    val request = Request.Builder()
        .url(apiUrl)
        .addHeader("Authorization", "Bearer $idToken") // Include the ID token
        .addHeader("Content-Type", "application/json")
        .post(requestBody)
        .build()

    // Execute the request asynchronously
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e.message ?: "Unknown error")
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                onFailure("Request failed: ${response.code}")
            } else {
                onSuccess()
            }
        }
    })
}
