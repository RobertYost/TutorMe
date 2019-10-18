package com.example.tutorme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {

    private lateinit var editButton: Button
    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var schoolTextView: TextView
    private lateinit var currencyTextView: TextView
    private lateinit var profilePictureUrlTextView: TextView
    private lateinit var passwordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        editButton = findViewById(R.id.settings_edit_button)
        firstNameTextView = findViewById(R.id.settings_first_name)
        lastNameTextView = findViewById(R.id.settings_last_name)
        emailTextView = findViewById(R.id.settings_email)
        schoolTextView = findViewById(R.id.settings_school)
        profilePictureUrlTextView = findViewById(R.id.settings_profile_picture_url)
        passwordTextView = findViewById(R.id.settings_password)

        editButton.setOnClickListener{
            val intent = Intent(this, EditSettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
