package com.capstone.bindetective.api

import com.capstone.bindetective.model.ArticleResponse
import com.capstone.bindetective.model.ArticleResponseItem
import com.capstone.bindetective.model.CreateUserResponse
import com.capstone.bindetective.model.PredictHistoryItem
import com.capstone.bindetective.model.PredictHistoryResponse
import com.capstone.bindetective.model.PredictResponse
import com.capstone.bindetective.model.QuizDetailResponse
import com.capstone.bindetective.model.QuizResponseItem
import com.capstone.bindetective.model.SubmitQuizRequest
import com.capstone.bindetective.model.SubmitQuizResponse
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
    fun getPredictHistory(@Query("userId") userId: String): Call<PredictHistoryResponse>

    @GET("quizzes")
    fun getAllQuizzes(): Call<List<QuizResponseItem>>

    @GET("quizzes/{quizId}")
    fun getQuizDetail(
        @Path("quizId") quizId: String): Call<QuizDetailResponse>

    @POST("quizzes/{quizId}/submit")
    fun submitQuizAnswers(
        @Path("quizId") quizId: String,
        @Body request: SubmitQuizRequest
    ): Call<SubmitQuizResponse>
}
