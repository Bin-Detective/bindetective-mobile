package com.capstone.bindetective.api

import com.capstone.bindetective.model.ArticleResponse
import com.capstone.bindetective.model.CreateUserResponse
import com.capstone.bindetective.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // POST endpoint to create a user
    @POST("users")
    fun createUser(
        @Body user: User, // The user data to create a new user
        @Header("Authorization") authToken: String // Authorization header with Bearer token
    ): Call<CreateUserResponse>

    // GET endpoint to fetch all articles
    @GET("articles")
    fun getAllArticles(
        @Header("Authorization") authToken: String // Authorization header with Bearer token
    ): Call<List<ArticleResponse>>  // Fetch all articles
}
