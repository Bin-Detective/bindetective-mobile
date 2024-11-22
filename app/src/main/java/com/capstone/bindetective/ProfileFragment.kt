package com.capstone.bindetective

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ProfileFragment : Fragment() {

    private lateinit var editName: EditText
    private lateinit var editBirthDate: EditText
    private lateinit var saveButton: Button
    private var idToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        // Retrieve Token ID from arguments
        idToken = arguments?.getString("idToken")

        // Initialize UI components
        editName = rootView.findViewById(R.id.editName)
        editBirthDate = rootView.findViewById(R.id.editBirthDate)
        saveButton = rootView.findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val newName = editName.text.toString()
            val newBirthDate = editBirthDate.text.toString()

            if (idToken != null && newName.isNotEmpty() && newBirthDate.isNotEmpty()) {
                updateUserProfile(idToken!!, newName, newBirthDate)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView
    }

    private fun updateUserProfile(idToken: String, name: String, birthDate: String) {
        val apiUrl = "https://bindetective-1-2-476908432279.asia-southeast2.run.app/users" // Update your API URL

        // Create JSON data for the PUT body
        val jsonData = JSONObject().apply {
            put("userName", name)
            put("dateOfBirth", birthDate)
        }

        // Create the request body
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = jsonData.toString().toRequestBody(mediaType)

        // Create the HTTP client
        val client = OkHttpClient()

        // Build the request
        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer $idToken") // Include the ID token
            .addHeader("Content-Type", "application/json")
            .put(requestBody)
            .build()

        // Execute the request asynchronously
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                requireActivity().runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        // Close fragment after success
                        parentFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Request failed: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance(idToken: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString("idToken", idToken)
            fragment.arguments = args
            return fragment
        }
    }
}
