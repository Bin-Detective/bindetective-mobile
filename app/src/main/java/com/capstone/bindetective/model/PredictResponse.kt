package com.capstone.bindetective.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PredictResponse(
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("predicted_class") val predictedClass: String?,
    @SerializedName("waste_type") val wasteType: String?,
    @SerializedName("probabilities") val probabilities: Map<String, Double>
) : Serializable

