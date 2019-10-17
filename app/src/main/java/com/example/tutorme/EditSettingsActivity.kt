package com.example.tutorme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EditSettingsActivity : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var schoolEditText: EditText
    private lateinit var currencyEditText: EditText
    private lateinit var profilePicUrlEditText: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_settings)

        saveButton = findViewById(R.id.edit_settings_save_button)
        firstNameEditText = findViewById(R.id.edit_settings_first_name)
        lastNameEditText = findViewById(R.id.edit_settings_last_name)
        emailEditText = findViewById(R.id.edit_settings_email)
        passwordEditText = findViewById(R.id.edit_settings_password)
        schoolEditText = findViewById(R.id.edit_settings_school)
        currencyEditText = findViewById(R.id.edit_settings_currency)
        profilePicUrlEditText = findViewById(R.id.edit_settings_profile_pic)

        saveButton.setOnClickListener {
            //TODO: save entered settings
        }
    }
}
