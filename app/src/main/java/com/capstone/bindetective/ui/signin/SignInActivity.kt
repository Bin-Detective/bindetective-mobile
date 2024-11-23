package com.capstone.bindetective.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.capstone.bindetective.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AppCompatActivity
import com.capstone.bindetective.ui.main.BottomMainActivity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ktx.auth

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userInfoTextView: TextView
    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize UI elements
        userInfoTextView = findViewById(R.id.tv_user_info)
        signInButton = findViewById(R.id.btn_google_sign_in)
        signOutButton = findViewById(R.id.btn_google_sign_out)

        // Set up listeners
        signInButton.setOnClickListener { signIn() }
        signOutButton.setOnClickListener { signOut() }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        // Sign out from Firebase and Google
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            Log.d(TAG, "User signed out.")
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, BottomMainActivity::class.java)
            intent.putExtra("USER_NAME", user.displayName)
            intent.putExtra("USER_EMAIL", user.email)
            startActivity(intent)
            finish()
        } else {
            userInfoTextView.text = "User info will appear here"
            signInButton.visibility = Button.VISIBLE
            signOutButton.visibility = Button.GONE
        }
    }

    companion object {
        private const val TAG = "GoogleSignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}
