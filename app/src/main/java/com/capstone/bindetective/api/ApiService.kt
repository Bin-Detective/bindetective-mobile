package com.capstone.bindetective.api

import com.capstone.bindetective.model.ArticleResponse
import com.capstone.bindetective.model.ArticleResponseItem
import com.capstone.bindetective.model.CreateUserResponse
import com.capstone.bindetective.model.PredictHistoryItem
import com.capstone.bindetective.model.PredictHistoryResponse
import com.capstone.bindetective.model.PredictResponse
import com.capstone.bindetective.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // POST endpoint to create a user
    @POST("users")
    fun createUser(
        @Body user: User, // The user data to create a new user
    ): Call<CreateUserResponse>

    // GET endpoint to fetch all articles
    @GET("articles")
    fun getAllArticles(): Call<List<ArticleResponseItem>>

    @Multipart
    @POST("predict")
    fun predictImage(
        @Part image: MultipartBody.Part
    ): Call<PredictResponse>

    @GET("predict/collections")
    fun getPredictHistory(@Query("userId") userId: String): Call<List<PredictHistoryItem>>


}
