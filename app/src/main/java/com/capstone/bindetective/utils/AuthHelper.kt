package com.capstone.bindetective.utils

import com.google.firebase.auth.FirebaseAuth

// Helper function to get Firebase ID token
fun getIdToken(callback: (String?) -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val idToken = task.result?.token
            callback(idToken) // Pass the token back via callback
        } else {
            callback(null) // Handle failure
        }
    }
}
