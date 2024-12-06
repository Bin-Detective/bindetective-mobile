package com.capstone.bindetective.ui.camera

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bindetective.api.ApiConfig
import com.capstone.bindetective.model.PredictResponse
import com.capstone.bindetective.utils.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CameraViewModel(context: Context) : ViewModel() {

    private val _predictionResult = MutableLiveData<Result<PredictResponse>>()
    val predictionResult: LiveData<Result<PredictResponse>> get() = _predictionResult

    val apiService = ApiConfig.getApiService()

    fun predictImage(file: File) {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.name, requestBody)

        apiService.predictImage(part).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _predictionResult.value = Result.Success(response.body()!!)
                } else {
                    _predictionResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _predictionResult.value = Result.Error(t.message ?: "Prediction API call failed")
            }
        })
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
                return CameraViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
