package com.capstone.bindetective.ui.quizdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.QuizDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizDetailViewModel : ViewModel() {

    private val _quizDetail = MutableLiveData<QuizDetailResponse>()
    val quizDetail: LiveData<QuizDetailResponse> get() = _quizDetail

    fun getQuizDetail(quizId: String) {
        val apiService = ApiConfig.getApiService()
        apiService.getQuizDetail(quizId)
            .enqueue(object : Callback<QuizDetailResponse> {
                override fun onResponse(
                    call: Call<QuizDetailResponse>,
                    response: Response<QuizDetailResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _quizDetail.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<QuizDetailResponse>, t: Throwable) {
                    Log.e("QuizDetailViewModel", "Failed to get quiz details: ${t.message}")
                }
            })
    }
}
