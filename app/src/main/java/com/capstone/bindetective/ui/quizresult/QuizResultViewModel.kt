package com.capstone.bindetective.ui.quizresult

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.SubmitQuizRequest
import com.capstone.bindetective.model.SubmitQuizResponse
import retrofit2.Call
import retrofit2.Response

class QuizResultViewModel : ViewModel() {
    val submitQuizResult = MutableLiveData<SubmitQuizResponse?>()

    fun submitQuizAnswers(quizId: String, request: SubmitQuizRequest) {
        ApiConfig.getApiService().submitQuizAnswers(quizId, request)
            .enqueue(object : retrofit2.Callback<SubmitQuizResponse> {
                override fun onResponse(
                    call: Call<SubmitQuizResponse>,
                    response: Response<SubmitQuizResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()
                        submitQuizResult.postValue(responseBody)
                        Log.d("QuizResultViewModel", "Quiz answers submitted successfully: $responseBody")
                    } else {
                        Log.e("QuizResultViewModel", "Error: ${response.errorBody()?.string()}")
                        submitQuizResult.postValue(SubmitQuizResponse(message = "Submission failed", score = null))
                    }
                }

                override fun onFailure(call: Call<SubmitQuizResponse>, t: Throwable) {
                    Log.e("QuizResultViewModel", "API call failed: ${t.message}")
                    submitQuizResult.postValue(SubmitQuizResponse(message = "Network error: ${t.message}", score = null))
                }
            })
    }
}
