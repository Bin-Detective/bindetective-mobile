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

        holder.tvPredictedClass.text = "Predicted Class: ${item.predictedClass}"
        holder.tvWasteType.text = "Waste Type: ${item.wasteType}"
        holder.tvTimestamp.text = "Timestamp: ${item.timestamp}"

        Log.d("CollectionAdapter", "Loading image: ${item.imageUrl}")

        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        Log.d("CollectionAdapter", "Item count: ${predictHistoryList.size}")
        return predictHistoryList.size
    }
}
