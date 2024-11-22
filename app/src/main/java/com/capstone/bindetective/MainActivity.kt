package com.capstone.bindetective

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve data passed via Intent
        val displayName = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")

        // Find UI components for displaying user data
        val textView = findViewById<TextView>(R.id.textView)  // Make sure you have the right TextView in the layout
        val emailTextView = findViewById<TextView>(R.id.emailTextView) // TextView for email

        // Set the text of TextViews to show user data
        textView.text = "Name: $displayName"
        emailTextView.text = "Email: $email"

        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener {
            val birthDate = findViewById<EditText>(R.id.editTextBirthdate).text.toString()
            if (birthDate.isNotEmpty()) {
                // Get the current user and retrieve their ID Token
                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result?.token
                        if (idToken != null) {
                            // Redirect to HomeActivity with data
                            val intent = Intent(this, HomeActivity::class.java).apply {
                                putExtra("name", displayName)
                                putExtra("email", email)
                                putExtra("birthDate", birthDate)
                                putExtra("idToken", idToken) // Pass Token ID
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to retrieve ID token", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Failed to retrieve ID token", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a valid birthdate", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
