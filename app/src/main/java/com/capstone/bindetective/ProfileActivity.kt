package com.capstone.bindetective

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editBirthDate: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Retrieve Token ID from Intent
        val idToken = intent.getStringExtra("idToken")

        // Initialize UI components
        editName = findViewById(R.id.editName)
        editBirthDate = findViewById(R.id.editBirthDate)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val newName = editName.text.toString()
            val newBirthDate = editBirthDate.text.toString()

            if (idToken != null && newName.isNotEmpty() && newBirthDate.isNotEmpty()) {
                updateUserProfile(idToken, newName, newBirthDate)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserProfile(idToken: String, name: String, birthDate: String) {
        val apiUrl = "https://bindetective-1-2-476908432279.asia-southeast2.run.app/users" // Update your API URL

        // Create JSON data for the PUT body
        val jsonData = JSONObject().apply {
            put("userName", name)
            put("dateOfBirth", birthDate)
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
            .put(requestBody)
            .build()

        // Execute the request asynchronously
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ProfileActivity, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close activity after success
                    } else {
                        Toast.makeText(this@ProfileActivity, "Request failed: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
