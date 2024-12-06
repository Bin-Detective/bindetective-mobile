package com.capstone.bindetective.ui.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.FragmentCameraBinding
import com.capstone.bindetective.ui.result.ResultFragment
import java.io.File
import java.io.FileOutputStream
import com.capstone.bindetective.utils.Result
import java.text.DecimalFormat

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val cameraViewModel: CameraViewModel by viewModels { CameraViewModel.Factory(requireContext()) }
    private var currentImagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        binding.btnGallery.setOnClickListener { openGallery() }

        observeViewModel()

        return binding.root
    }

    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 101) { // **Camera Capture**
                val bitmap = data.extras?.get("data") as? Bitmap
                if (bitmap != null) {
                    saveBitmapToFile(bitmap)?.let { file ->
                        cameraViewModel.predictImage(file)
                    }
                }
            } else if (requestCode == 102) {  // **Gallery Selection**
                val imageUri: Uri? = data.data
                if (imageUri != null) {
                    val filePath = requireContext().getExternalFilesDir(null)?.absolutePath
                    val selectedFile = File(filePath, "gallery_image.jpg")
                    requireContext().contentResolver.openInputStream(imageUri)?.use { input ->
                        FileOutputStream(selectedFile).use { output ->
                            input.copyTo(output)
                            cameraViewModel.predictImage(selectedFile)
                        }
                    }
                }
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File? {
        val path = requireContext().getExternalFilesDir(null)?.absolutePath ?: return null
        val file = File(path, "camera_image.jpg")

        try {
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Log.d("CameraFragment", "File path: ${file.absolutePath}")
            }
            return file
        } catch (e: Exception) {
            Log.e("CameraFragment", "Failed to save photo", e)
            return null
        }
    }

    private fun observeViewModel() {
        cameraViewModel.predictionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Log.d("CameraFragment", "Prediction Successful: ${result.data}")

                    // Format probabilities to avoid scientific notation
                    val decimalFormat = DecimalFormat("#.####################") // Up to 20 decimal places
                    val formattedProbabilities = result.data.probabilities.mapValues {
                        decimalFormat.format(it.value)
                    }
                    Log.d("CameraFragment", "Formatted Probabilities: $formattedProbabilities")

                    // Create a bundle and put the PredictResponse into it
                    val bundle = Bundle().apply {
                        putSerializable("predict_response", result.data)
                    }

                    // Manually replace the fragment transaction with ResultFragment
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, ResultFragment::class.java, bundle)
                        addToBackStack(null)
                        commit()
                    }
                }
                is Result.Error -> {
                    Log.e("CameraFragment", "API Error: ${result.message}")
                }
                else -> {
                    Log.d("CameraFragment", "Loading Prediction...")
                }
            }
        }
    }

}
