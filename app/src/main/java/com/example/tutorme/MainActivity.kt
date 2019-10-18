package com.example.tutorme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var createAccountButton: Button
    private lateinit var facebookLogInButton: ImageButton

    private lateinit var signInTextView: TextView


    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()

        Log.d(TAG, "onCreate()")

//        createAccountButton = findViewById(R.id.create_account_button)
//        facebookLogInButton = findViewById(R.id.facebook_log_in_button)
//        signInTextView = findViewById(R.id.sign_in_text_view)
//
//        createAccountButton.setOnClickListener {
//            val intent = Intent(this, RegistrationActivity::class.java)
//            startActivity(intent)
//        }
//        facebookLogInButton.setOnClickListener {
//            //TODO: log in with facebook
//        }
//        signInTextView.setOnClickListener {
//            //TODO: go to sign in page
//            //links to settings for testing
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // Navigate to new activity
//                finish()
            } else {
                // Sign in failed. If response is null the user canceled the sign-in flow using
                // the back button. Otherwise check response.getError().getErrorCode() and handle
                // the error
                Log.d(TAG, "Failed to sign in")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

}
