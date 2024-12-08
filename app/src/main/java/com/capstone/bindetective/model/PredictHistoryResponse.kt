package com.capstone.bindetective.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class PredictHistoryResponse(
    @SerializedName("predictHistory") val predictHistory: List<PredictHistoryItem>
) : Serializable

data class PredictHistoryItem(
    @SerializedName("id") val id: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("predicted_class") val predictedClass: String?,
    @SerializedName("waste_type") val wasteType: String?,
    @SerializedName("probabilities") val probabilities: Map<String, Float>,
    @SerializedName("timestamp") val timestamp: Timestamp, // Use nested object
    @SerializedName("userId") val userId: String
) : Serializable

// Timestamp object to map the nested JSON structure
data class Timestamp(
    @SerializedName("seconds") val seconds: Long,
    @SerializedName("nanoseconds") val nanoseconds: Int
) : Serializable {
    fun toFormattedDateTime(): String {
        val date = Date(seconds * 1000)
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
}
