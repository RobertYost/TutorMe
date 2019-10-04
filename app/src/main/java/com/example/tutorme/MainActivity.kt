package com.example.tutorme

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var createAccountButton: Button
    private lateinit var facebookLogInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)

        createAccountButton = findViewById(R.id.create_account_button)
        facebookLogInButton = findViewById(R.id.facebook_log_in_button)

        createAccountButton.setOnClickListener { view: View ->
            //TODO: go to account creation page
        }
        facebookLogInButton.setOnClickListener { view: View ->
            //TODO: log in with facebook
        }
    }

}
