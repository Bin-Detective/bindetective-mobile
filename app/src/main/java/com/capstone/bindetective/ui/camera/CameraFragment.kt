package com.capstone.bindetective.ui.camera

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import java.util.UUID
import android.os.Environment

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
        val cameraPermission = Manifest.permission.CAMERA
        val readStoragePermission = Manifest.permission.READ_MEDIA_IMAGES

        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(requireContext(), cameraPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(cameraPermission)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), readStoragePermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(readStoragePermission)
        }

        if (permissionsToRequest.isEmpty()) {
            openCameraIntent()
        } else {
            requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun openCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoUri = createImageFile() ?: return

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun createImageFile(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis() / 1000)
        }

        return requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    private fun saveCapturedImage(photoUri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(photoUri)
            val externalDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val capturedFile = File(externalDir, "captured_${System.currentTimeMillis()}.jpg")

            if (externalDir != null) {
                if (!externalDir.exists()) {
                    externalDir.mkdirs() // Ensure the directory exists
                }
            }

            inputStream.use { input ->
                FileOutputStream(capturedFile).use { output ->
                    if (input != null) {
                        input.copyTo(output)
                    }
                }
            }

            Log.d("CameraFragment", "Captured image saved at ${capturedFile.absolutePath}")
            selectedImageFile = capturedFile

            // Preview the captured image
            binding.ivPreview.setImageURI(photoUri)

        } catch (e: Exception) {
            Log.e("CameraFragment", "Failed to save captured image", e)
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

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            permissions.forEachIndexed { index, permission ->
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("CameraFragment", "Permission $permission granted")
                    Toast.makeText(requireContext(), "Permission $permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("CameraFragment", "Permission $permission denied")
                    Toast.makeText(requireContext(), "Permission $permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    saveCapturedImage(photoUri)
                }
                REQUEST_GALLERY -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        saveGalleryImage(imageUri)
                    }
                }
            }
        }
    }

    private fun saveGalleryImage(imageUri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(imageUri)
            val externalDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val selectedFile = File(externalDir, "gallery_image_${System.currentTimeMillis()}.jpg")

            if (externalDir != null) {
                if (!externalDir.exists()) {
                    externalDir.mkdirs()
                }
            }

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
