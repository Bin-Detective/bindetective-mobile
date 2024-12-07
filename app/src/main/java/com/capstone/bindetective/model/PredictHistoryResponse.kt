package com.capstone.bindetective.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PredictHistoryResponse(
    @SerializedName("predictHistory") val predictHistory: List<PredictHistoryItem>
) : Serializable

data class PredictHistoryItem(
    @SerializedName("id") val id: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("predicted_class") val predictedClass: String?,
    @SerializedName("waste_type") val wasteType: String?,
    @SerializedName("probabilities") val probabilities: Map<String, Float>,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("userId") val userId: String
) : Serializable
