package com.capstone.bindetective.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.bindetective.databinding.FragmentResultBinding
import com.capstone.bindetective.model.PredictResponse

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val response = arguments?.getSerializable("predict_response") as? PredictResponse

        response?.let {
            binding.tvPredictedClass.text = "Predicted Class: ${it.predictedClass}"
            binding.tvWasteType.text = "Waste Type: ${it.wasteType}"
            binding.tvProbabilities.text = it.probabilities.entries.joinToString("\n") { "${it.key}: ${it.value}" }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
