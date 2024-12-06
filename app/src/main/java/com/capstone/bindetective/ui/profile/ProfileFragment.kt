package com.capstone.bindetective.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.FragmentProfileBinding
import com.capstone.bindetective.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch the current user from Firebase Authentication
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            // Display user's name and email
            binding.textViewUserName.text = firebaseUser.displayName ?: "Unknown User"
            binding.textViewEmail.text = firebaseUser.email ?: "No email available"

            // Load profile picture using Picasso (or Glide)
            val profilePictureUrl = firebaseUser.photoUrl?.toString()
            if (profilePictureUrl != null) {
                Picasso.get().load(profilePictureUrl).into(binding.imageViewProfilePicture)
            } else {
                binding.imageViewProfilePicture.setImageResource(R.drawable.ic_profile_placeholder)
            }

            // Fetch user points (replace with actual logic if points are stored remotely or locally)
            fetchUserPoints()
        }

        // Set up Sign Out button functionality
        binding.buttonSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun fetchUserPoints() {
        // Replace with logic to fetch points from an API, database, or shared preferences
        binding.textViewPoints.text = "Points: 0"  // Example, replace with actual points
    }

    private fun signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // Show a success message and navigate to the Sign-In screen
        Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()

        // Navigate to Sign-In screen
        val intent = Intent(requireContext(), SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
