package com.capstone.bindetective.ui.home

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.bindetective.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Retrieve data passed from intent
        val displayName = intent.getStringExtra("name") ?: "Unknown User"
        val email = intent.getStringExtra("email") ?: "No Email"
        val birthDate = intent.getStringExtra("birthDate") ?: "Not Set"

        // Find UI components
        val nameTextView = findViewById<TextView>(R.id.userName)
        val userDetailsTextView = findViewById<TextView>(R.id.userDetails)
        val userPointsTextView = findViewById<TextView>(R.id.userPoints)
        val profileImageView = findViewById<ImageView>(R.id.userImage)

        // Set user details
        nameTextView.text = displayName
        userDetailsTextView.text = "Email: $email\nBirthdate: $birthDate"
        userPointsTextView.text = "Points: 0"

        // Set placeholder profile image
        profileImageView.setImageResource(R.drawable.ic_profile_placeholder)
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        // Replacing the current fragment with the selected one
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment) // Mengganti fragment di container
            .commit()
        return true
    }
}
