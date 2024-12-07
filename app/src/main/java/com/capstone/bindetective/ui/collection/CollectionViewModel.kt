package com.capstone.bindetective.ui.collection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.PredictHistoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionViewModel : ViewModel() {

    private val _predictHistory = MutableLiveData<List<PredictHistoryItem>?>()
    val predictHistory: MutableLiveData<List<PredictHistoryItem>?> get() = _predictHistory

    // Function to get prediction history for a user by userId
    fun getPredictHistory(userId: String) {
        Log.d("CollectionViewModel", "Initiating API call to get PredictHistory for userId: $userId")

        ApiConfig.getApiService().getPredictHistory(userId).enqueue(object : Callback<List<PredictHistoryItem>> {
            override fun onResponse(call: Call<List<PredictHistoryItem>>, response: Response<List<PredictHistoryItem>>) {
                if (response.isSuccessful) {
                    val predictHistoryList = response.body()

                    Log.d("CollectionViewModel", "API call successful. Received PredictHistoryList: $predictHistoryList")

                    if (predictHistoryList.isNullOrEmpty()) {
                        Log.e("CollectionViewModel", "API response contains an empty PredictHistoryList")
                    } else {
                        Log.d("CollectionViewModel", "Received ${predictHistoryList.size} items from API response")
                    }

                    _predictHistory.postValue(predictHistoryList)
                } else {
                    Log.e("CollectionViewModel", "API call failed with response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<PredictHistoryItem>>, t: Throwable) {
                Log.e("CollectionViewModel", "API call failed with error: ${t.message}")
            }
        })
    }
}
