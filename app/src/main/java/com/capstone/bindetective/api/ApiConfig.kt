package com.capstone.bindetective.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "https://bindetective-1-2-476908432279.asia-southeast2.run.app/"

    // Create and provide the ApiService instance
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for your API
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for converting JSON
            .build()
            .create(ApiService::class.java)
    }
}
