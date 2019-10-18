package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var firstNameTxt: EditText
    private lateinit var lastNameTxt: EditText
    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var passwordConfirmationTxt: EditText
    private lateinit var schoolSpinner: Spinner
    private lateinit var submitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firstNameTxt = findViewById(R.id.firstNameTxt)
        lastNameTxt = findViewById(R.id.lastNameTxt)
        emailTxt = findViewById(R.id.emailTxt)
        passwordTxt = findViewById(R.id.passwordTxt)
        passwordConfirmationTxt = findViewById(R.id.confirmPasswordTxt)
        schoolSpinner = findViewById(R.id.schoolSpinner)
        submitBtn = findViewById(R.id.submitBtn)

        submitBtn.setOnClickListener { view: View ->
            // TODO: Create account (May be obsolete with the new FirebaseAuth)
            val intent = Intent(this, SwipeActivity::class.java)
            startActivity(intent)
        }
    }
}
