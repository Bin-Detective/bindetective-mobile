package com.capstone.bindetective.ui.quizdetail

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

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Updated method without token
    fun getQuizDetail(quizId: String) {
        ApiConfig.getApiService().getQuizDetail(quizId)
            .enqueue(object : Callback<QuizDetailResponse> {
                override fun onResponse(
                    call: Call<QuizDetailResponse>,
                    response: Response<QuizDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        _quizDetail.postValue(response.body())
                    } else {
                        _errorMessage.postValue("Failed to get quiz details")
                    }
                }

                override fun onFailure(call: Call<QuizDetailResponse>, t: Throwable) {
                    _errorMessage.postValue(t.message)
                }
            })
    }
}
