package com.capstone.bindetective.ui.quizresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.SubmitQuizRequest
import com.capstone.bindetective.model.SubmitQuizResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizResultViewModel : ViewModel() {

    private val _submitQuizResult = MutableLiveData<SubmitQuizResponse>()
    val submitQuizResult: LiveData<SubmitQuizResponse> get() = _submitQuizResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun submitQuizAnswers(quizId: String, userId: String, answers: List<SubmitQuizRequest.Answer>) {
        val submitRequest = SubmitQuizRequest(userId, answers)

        ApiConfig.getApiService().submitQuizAnswers(quizId, submitRequest).enqueue(object : Callback<SubmitQuizResponse> {
            override fun onResponse(
                call: Call<SubmitQuizResponse>,
                response: Response<SubmitQuizResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _submitQuizResult.postValue(response.body())
                } else {
                    _errorMessage.postValue("Submission failed with error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SubmitQuizResponse>, t: Throwable) {
                _errorMessage.postValue("Network error: ${t.message}")
            }
        })
    }
}
