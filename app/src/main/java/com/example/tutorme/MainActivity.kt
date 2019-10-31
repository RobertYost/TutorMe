package com.example.tutorme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorme.swipe_view.SwipeActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.FirestoreClient


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .whereEqualTo("email", FirebaseAuth.getInstance().currentUser?.email)
                    .get()
                    .addOnSuccessListener {
                        val intent = if (it.isEmpty) {
                            Intent(this, EditSettingsActivity::class.java)
                            intent.putExtra(
                                "user_email",
                                FirebaseAuth.getInstance().currentUser?.email
                            )
                        } else {
                            Intent(this, SwipeActivity::class.java)
                        }
                        Log.d(TAG, it.toString())
                        startActivity(intent)
                    }
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
