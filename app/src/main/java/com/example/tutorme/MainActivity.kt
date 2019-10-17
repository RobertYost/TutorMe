package com.example.tutorme

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var createAccountButton: Button
    private lateinit var facebookLogInButton: ImageButton

    private lateinit var signInTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)

        Log.d(TAG, "onCreate()")

        createAccountButton = findViewById(R.id.create_account_button)
        facebookLogInButton = findViewById(R.id.facebook_log_in_button)
        signInTextView = findViewById(R.id.sign_in_text_view)

        createAccountButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        facebookLogInButton.setOnClickListener {
            //TODO: log in with facebook
        }
        signInTextView.setOnClickListener {
            //TODO: go to sign in page
            //links to settings for testing
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart()")
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

}
