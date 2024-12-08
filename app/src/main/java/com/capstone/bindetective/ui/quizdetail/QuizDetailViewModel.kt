package com.capstone.bindetective.ui.quizdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.QuizDetailResponse
import com.capstone.bindetective.model.SubmitQuizRequest
import com.capstone.bindetective.model.SubmitQuizResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizDetailViewModel : ViewModel() {

    private val _quizDetail = MutableLiveData<QuizDetailResponse>()
    val quizDetail: LiveData<QuizDetailResponse> get() = _quizDetail

    private val _submitResponse = MutableLiveData<SubmitQuizResponse>()
    val submitResponse: LiveData<SubmitQuizResponse> get() = _submitResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

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
                    } else {
                        _errorMessage.postValue("Failed to retrieve quiz details. Error code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<QuizDetailResponse>, t: Throwable) {
                    _errorMessage.postValue("Error: ${t.message}")
                }
            })
    }

    fun submitQuizAnswers(quizId: String, request: SubmitQuizRequest, callback: (SubmitQuizResponse) -> Unit) {
        val apiService = ApiConfig.getApiService()
        apiService.submitQuizAnswers(quizId, request)
            .enqueue(object : Callback<SubmitQuizResponse> {
                override fun onResponse(
                    call: Call<SubmitQuizResponse>,
                    response: Response<SubmitQuizResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _submitResponse.postValue(response.body())
                        callback(response.body()!!)
                    } else {
                        _errorMessage.postValue("Failed to submit quiz answers. Error code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<SubmitQuizResponse>, t: Throwable) {
                    _errorMessage.postValue("Error: ${t.message}")
                }
            })
    }

}
