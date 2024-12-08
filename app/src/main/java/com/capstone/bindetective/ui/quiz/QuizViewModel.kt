package com.capstone.bindetective.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.QuizResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizViewModel : ViewModel() {

    private val _quizzes = MutableLiveData<List<QuizResponseItem>>()
    val quizzes: LiveData<List<QuizResponseItem>> get() = _quizzes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchQuizzes() {
        ApiConfig.getApiService().getAllQuizzes().enqueue(object : Callback<List<QuizResponseItem>> {
            override fun onResponse(
                call: Call<List<QuizResponseItem>>,
                response: Response<List<QuizResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _quizzes.postValue(response.body())
                } else {
                    _error.postValue("Failed to fetch quizzes")
                }
            }

            override fun onFailure(call: Call<List<QuizResponseItem>>, t: Throwable) {
                _error.postValue("Network error: ${t.message}")
            }
        })
    }
}
