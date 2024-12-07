package com.capstone.bindetective.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import java.util.UUID
import android.os.Environment
import java.io.IOException
import java.io.FileInputStream
import android.content.ContentValues
import android.provider.MediaStore
import androidx.core.net.toUri

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val cameraViewModel: CameraViewModel by viewModels { CameraViewModel.Factory(requireContext()) }
    private var selectedImageFile: File? = null
    private lateinit var photoUri: Uri

    companion object {
        private const val REQUEST_CAMERA = 101
        private const val REQUEST_GALLERY = 102
        private const val REQUEST_CAMERA_PERMISSION = 103
        private const val REQUEST_WRITE_STORAGE_PERMISSION = 104
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        binding.btnGallery.setOnClickListener { openGallery() }
        binding.btnCamera.setOnClickListener { takePicture() }
        binding.btnUpload.setOnClickListener { uploadImage() }

        observeViewModel()

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    private fun takePicture() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val photoFile = createImageFile()

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    photoFile
                )

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_CAMERA)
            }
        } else {
            requestPermissions(missingPermissions.toTypedArray(), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun createImageFile(): File? {
        return try {
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File(storageDir, "photo_${System.currentTimeMillis()}.jpg")

            Log.d("CameraFragment", "File path: ${imageFile.absolutePath}")
            Log.d("CameraFragment", "Storage Directory: ${storageDir?.absolutePath}")

            imageFile.createNewFile()
            imageFile
        } catch (e: IOException) {
            Log.e("CameraFragment", "Failed to create file", e)
            null
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
                    val decimalFormat = DecimalFormat("#.####################")

                    val topProbabilities = result.data.probabilities.entries
                        .sortedByDescending { it.value }
                        .take(3)
                        .map { "${it.key}: ${decimalFormat.format(it.value)}" }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                permissions.forEachIndexed { index, permission ->
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("CameraFragment", "Permission $permission granted")
                    } else {
                        Log.e("CameraFragment", "Permission $permission denied")
                        Toast.makeText(requireContext(), "Permission $permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            REQUEST_WRITE_STORAGE_PERMISSION -> {
                permissions.forEachIndexed { index, permission ->
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("CameraFragment", "Write storage permission $permission granted")
                    } else {
                        Log.e("CameraFragment", "Write storage permission $permission denied")
                        Toast.makeText(requireContext(), "Write storage permission $permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        saveGalleryImage(imageUri)
                    }
                }
                REQUEST_CAMERA -> {
                    binding.ivPreview.setImageURI(photoUri)
                }
            }
        }
    }

    private fun saveGalleryImage(imageUri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val externalDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val selectedFile = File(externalDir, "gallery_image.jpg")

            inputStream.use { input ->
                FileOutputStream(selectedFile).use { output ->
                    if (input != null) {
                        input.copyTo(output)
                    }
                }
                Log.d("CameraFragment", "Gallery image saved at ${selectedFile.absolutePath}")
                selectedImageFile = selectedFile
                binding.ivPreview.setImageURI(imageUri)
            }
        } catch (e: Exception) {
            Log.e("CameraFragment", "Failed to save gallery image", e)
        }
    }
}
