package com.capstone.bindetective.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.bindetective.R
import com.capstone.bindetective.databinding.ActivityBottomMainBinding
import com.capstone.bindetective.ui.collection.CollectionFragment
import com.capstone.bindetective.ui.camera.CameraFragment
import com.capstone.bindetective.ui.home.HomeFragment
import com.capstone.bindetective.ui.profile.ProfileFragment
import com.capstone.bindetective.ui.quiz.QuizFragment

class BottomMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityBottomMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Handle BottomNavigationView item selection
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_collection -> CollectionFragment()
                R.id.nav_camera -> CameraFragment()
                R.id.nav_quiz -> QuizFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }
            loadFragment(selectedFragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}