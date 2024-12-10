package com.capstone.bindetective.ui.result

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.FragmentResultBinding
import com.capstone.bindetective.model.PredictResponse
import com.capstone.bindetective.ui.camera.CameraFragment

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

        // Back button click listener
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, CameraFragment())
                addToBackStack(null)  // Optional: Ensure it remains in the back stack
                commit()
            }
        }

        val predictionResult = arguments?.getSerializable("predict_response") as? PredictResponse
        val imageUriString = arguments?.getString("image_uri")

        predictionResult?.let {
            val predictedClass = it.predictedClass
            val predictedClassProbability = (it.probabilities[predictedClass] ?: 0.0) * 100.0

            // Set the Predicted Class with Percentage
            binding.tvPredictedClass.text = "Predicted Class: $predictedClass ${predictedClassProbability.toInt()}%"
            binding.tvWasteType.text = "Waste Type: ${it.wasteType}"
        }

        if (imageUriString != null) {
            binding.ivPreview.setImageURI(Uri.parse(imageUriString))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
