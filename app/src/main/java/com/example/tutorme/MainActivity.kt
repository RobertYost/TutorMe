package com.example.tutorme

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var createAccountButton: Button
    private lateinit var facebookLogInButton: ImageButton

    private lateinit var signInTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)

        createAccountButton = findViewById(R.id.create_account_button)
        facebookLogInButton = findViewById(R.id.facebook_log_in_button)
        signInTextView = findViewById(R.id.sign_in_text_view)

        createAccountButton.setOnClickListener { view: View ->
            val intent = Intent(this, RegistrationActivity::class.java)
            // start your next activity
            startActivity(intent)
        }
        facebookLogInButton.setOnClickListener { view: View ->
            //TODO: log in with facebook
        }
        signInTextView.setOnClickListener {view: View ->
            //TODO: go to sign in page
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("STATE", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.i("STATE", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("STATE", "onDestroy")
    }

}
