package com.capstone.bindetective.ui.collection

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.bindetective.R
import com.capstone.bindetective.model.PredictHistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CollectionAdapter(private val predictHistoryList: List<PredictHistoryItem>) :
    RecyclerView.Adapter<CollectionAdapter.PredictHistoryViewHolder>() {

    private val sortedPredictHistoryList: List<PredictHistoryItem> = predictHistoryList
        .sortedByDescending { it.timestamp.seconds }  // Sort by timestamp in descending order

    inner class PredictHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvPredictedClass: TextView = itemView.findViewById(R.id.tvPredictedClass)
        val tvWasteType: TextView = itemView.findViewById(R.id.tvWasteType)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_collection, parent, false)
        return PredictHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictHistoryViewHolder, position: Int) {
        val item = sortedPredictHistoryList.getOrNull(position)

        if (item == null) {
            Log.e("CollectionAdapter", "Item at position $position is null")
            return
        }

        Log.d("CollectionAdapter", "Binding item at position $position: $item")

        // Calculate percentage probability
        val predictedClass = item.predictedClass
        val predictedClassProbability = (item.probabilities[predictedClass] ?: 0.0) * 100.0

        // Set Predicted Class with Percentage
        holder.tvPredictedClass.text = "Predicted Class: $predictedClass ${predictedClassProbability.toInt()}%"
        holder.tvWasteType.text = "Waste Type: ${item.wasteType}"

        val formattedTimestamp = item.timestamp.toFormattedDateTime()
        holder.tvTimestamp.text = formattedTimestamp

        Log.d("CollectionAdapter", "Loading image: ${item.imageUrl}")

        // Load image using Glide
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        Log.d("CollectionAdapter", "Item count: ${sortedPredictHistoryList.size}")
        return sortedPredictHistoryList.size
    }
}
