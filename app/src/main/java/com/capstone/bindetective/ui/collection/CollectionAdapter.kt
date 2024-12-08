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

    init {
        Log.d("CollectionAdapter", "Adapter initialized with ${predictHistoryList.size} items")
    }

    inner class PredictHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvPredictedClass: TextView = itemView.findViewById(R.id.tvPredictedClass)
        val tvWasteType: TextView = itemView.findViewById(R.id.tvWasteType)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictHistoryViewHolder {
        Log.d("CollectionAdapter", "Creating ViewHolder for position $viewType")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_collection, parent, false)
        return PredictHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictHistoryViewHolder, position: Int) {
        val item = predictHistoryList.getOrNull(position)

        if (item == null) {
            Log.e("CollectionAdapter", "Item at position $position is null")
            return
        }

        Log.d("CollectionAdapter", "Binding item at position $position: $item")

        // Set Predicted Class and Waste Type
        holder.tvPredictedClass.text = "Predicted Class: ${item.predictedClass}"
        holder.tvWasteType.text = "Waste Type: ${item.wasteType}"

        // Call it now from your adapter
        val formattedTimestamp = item.timestamp.toFormattedDateTime()
        holder.tvTimestamp.text = formattedTimestamp

        Log.d("CollectionAdapter", "Loading image: ${item.imageUrl}")

        // Load image using Glide
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        Log.d("CollectionAdapter", "Item count: ${predictHistoryList.size}")
        return predictHistoryList.size
    }

    internal fun String.toFormattedDateTime(): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val date = parser.parse(this)
            val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            formatter.format(date!!)
        } catch (e: Exception) {
            Log.e("CollectionAdapter", "Invalid timestamp format", e)
            "Invalid Date"
        }
    }
}
