package com.capstone.bindetective.ui.collection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.PredictHistoryItem
import com.capstone.bindetective.model.PredictHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionViewModel : ViewModel() {

    private val _predictHistory = MutableLiveData<List<PredictHistoryItem>?>()
    val predictHistory: LiveData<List<PredictHistoryItem>?> get() = _predictHistory

    fun getPredictHistory(userId: String) {
        ApiConfig.getApiService().getPredictHistory(userId).enqueue(object : Callback<PredictHistoryResponse> {

            override fun onResponse(call: Call<PredictHistoryResponse>, response: Response<PredictHistoryResponse>) {
                if (response.isSuccessful) {
                    val predictHistoryResponse = response.body()

                    // Filter the response to include only a specific user ID
                    val filteredHistory = predictHistoryResponse?.predictHistory?.filter {
                        it.userId == "u7pzdJ3XGsNrtoQqx5ujUXqYOoJ3"
                    }

                    filteredHistory?.forEach {
                        Log.d("History", "Timestamp: ${it.timestamp.toFormattedDateTime()}")
                    }

                    _predictHistory.postValue(filteredHistory)
                } else {
                    Log.e("API ERROR", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PredictHistoryResponse>, t: Throwable) {
                Log.e("API FAILURE", t.message ?: "Unknown error")
            }
        })
    }
}
