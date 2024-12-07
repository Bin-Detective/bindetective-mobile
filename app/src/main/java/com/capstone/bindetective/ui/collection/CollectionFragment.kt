package com.capstone.bindetective.ui.collection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.bindetective.ui.collection.CollectionAdapter
import com.capstone.bindetective.R
import com.capstone.bindetective.model.PredictHistoryItem
import com.capstone.bindetective.ui.collection.CollectionViewModel

class CollectionFragment : Fragment() {

    private val viewModel: CollectionViewModel by viewModels()
    private lateinit var adapter: CollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CollectionAdapter(emptyList())
        recyclerView.adapter = adapter

        val userId = "u7pzdJ3XGsNrtoQqx5ujUXqYOoJ3"

        Log.d("CollectionFragment", "Fetching Predict History for userId: $userId")

        // Observe the LiveData from the ViewModel
        viewModel.getPredictHistory(userId)

        viewModel.predictHistory.observe(viewLifecycleOwner) { predictHistoryList ->
            Log.d("CollectionFragment", "Received PredictHistoryList: $predictHistoryList")

            if (predictHistoryList.isNullOrEmpty()) {
                Log.e("CollectionFragment", "No items found in PredictHistoryList")
            } else {
                Log.d("CollectionFragment", "Updating RecyclerView with ${predictHistoryList.size} items")
            }

            adapter = predictHistoryList?.let { CollectionAdapter(it) }!!
            recyclerView.adapter = adapter
        }
    }
}
