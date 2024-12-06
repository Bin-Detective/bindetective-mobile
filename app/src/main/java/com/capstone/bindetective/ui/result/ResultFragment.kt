package com.capstone.bindetective.ui.result

import android.net.Uri
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
        super.onViewCreated(view, savedInstanceState)

        val predictionResult = arguments?.getSerializable("predict_response") as? PredictResponse
        val imageUriString = arguments?.getString("image_uri")

        predictionResult?.let {
            binding.tvPredictedClass.text = "Predicted Class: ${it.predictedClass}"
            binding.tvWasteType.text = "Waste Type: ${it.wasteType}"

            // Get top 3 probabilities
            val topProbabilities = it.probabilities.entries
                .sortedByDescending { it.value }
                .take(3)
                .joinToString(", ") { "${it.key}=${"%.2f".format(it.value)}" }

            binding.tvProbabilities.text = "Top Probabilities: $topProbabilities"
        }

        // Display the original selected image
        if (imageUriString != null) {
            binding.ivPreview.setImageURI(Uri.parse(imageUriString))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
