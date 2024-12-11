package com.capstone.bindetective.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.QuizResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("quiz_data", Application.MODE_PRIVATE)

    private val _quizzes = MutableLiveData<List<QuizResponseItem>>()
    val quizzes: LiveData<List<QuizResponseItem>> get() = _quizzes

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val userId = "u7pzdJ3XGsNrtoQqx5ujUXqYOoJ3"  // Replace with actual user ID

    // Save the score in SharedPreferences
    fun saveScore(score: Int?) {
        score?.let {
            sharedPreferences.edit().putInt("SAVED_SCORE_$userId", it).apply()
            _score.postValue(it)
        }
    }

    // Retrieve the score from SharedPreferences
    fun getScore(): Int = sharedPreferences.getInt("SAVED_SCORE_$userId", 0)

    // Fetch all quizzes from the API
    fun fetchQuizzes() {
        ApiConfig.getApiService().getAllQuizzes().enqueue(object : Callback<List<QuizResponseItem>> {
            override fun onResponse(call: Call<List<QuizResponseItem>>, response: Response<List<QuizResponseItem>>) {
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
