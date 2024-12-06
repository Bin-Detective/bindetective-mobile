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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.FragmentCameraBinding
import com.capstone.bindetective.ui.result.ResultFragment
import com.capstone.bindetective.model.PredictResponse
import com.capstone.bindetective.utils.Result
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val cameraViewModel: CameraViewModel by viewModels { CameraViewModel.Factory(requireContext()) }
    private var selectedImageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        binding.btnGallery.setOnClickListener { openGallery() }
        binding.btnUpload.setOnClickListener { uploadImage() }

        observeViewModel()

        return binding.root
    }

    private fun openGallery() {
        Log.d("CameraFragment", "Opening gallery...")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("CameraFragment", "onActivityResult called: requestCode=$requestCode, resultCode=$resultCode")

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 102) { // Gallery Selection
                Log.d("CameraFragment", "Gallery selection detected")
                val imageUri: Uri? = data.data

                if (imageUri != null) {
                    try {
                        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                        val filePath = requireContext().getExternalFilesDir(null)?.absolutePath
                        val selectedFile = File(filePath, "gallery_image.jpg")

                        inputStream.use { input ->
                            FileOutputStream(selectedFile).use { output ->
                                if (input != null) {
                                    input.copyTo(output)
                                }
                                Log.d("CameraFragment", "Gallery image copied to file: ${selectedFile.absolutePath}")
                                selectedImageFile = selectedFile

                                // Show the selected image in the preview UI
                                binding.ivPreview.setImageURI(imageUri)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CameraFragment", "Failed to save gallery image", e)
                    }
                }
            }
        }
    }

    private fun uploadImage() {
        val fileToUpload = selectedImageFile
        if (fileToUpload != null) {
            Log.d("CameraFragment", "Uploading file: ${fileToUpload.absolutePath}")
            cameraViewModel.predictImage(fileToUpload)
        } else {
            Log.e("CameraFragment", "No file selected for upload")
        }
    }

    private fun observeViewModel() {
        cameraViewModel.predictionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    Log.d("CameraFragment", "Prediction Successful: ${result.data}")

                    val decimalFormat = DecimalFormat("#.####################")

                    // Sort probabilities by descending order and take the top 3
                    val topProbabilities = result.data.probabilities.entries
                        .sortedByDescending { it.value }
                        .take(3)
                        .map { "${it.key}: ${decimalFormat.format(it.value)}" }

                    Log.d("CameraFragment", "Top Probabilities: $topProbabilities")

                    // Pass the PredictResponse and the image URI to the ResultFragment
                    val bundle = Bundle().apply {
                        putSerializable("predict_response", result.data)
                        putString("image_uri", selectedImageFile?.toUri().toString())
                        putStringArrayList("top_probabilities", ArrayList(topProbabilities))
                    }

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
