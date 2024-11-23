package com.capstone.bindetective.remote

import com.capstone.bindetective.model.CreateUserResponse
import com.capstone.bindetective.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiService {
    // POST endpoint to create a user
    @POST("users")
    fun createUser(
        @Body user: User, // The user data to create a new user
        @Header("Authorization") authToken: String // Authorization header with Bearer token
    ): Call<CreateUserResponse>
}
