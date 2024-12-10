package com.capstone.bindetective.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.capstone.bindetective.R
import com.capstone.bindetective.ui.signin.SignInActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Reference to the Login button
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Set click listener to navigate to SignInActivity
        btnLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
