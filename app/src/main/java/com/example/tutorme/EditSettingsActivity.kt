package com.example.tutorme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorme.databinding.ActivityEditSettingsBinding

class EditSettingsActivity : AppCompatActivity() {

    // Using view-binding from arch-components (requires Android Studio 3.6 Canary 11+)
    private lateinit var binding: ActivityEditSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.editSettingsSaveButton.setOnClickListener {
            //TODO: save entered settings
        }
    }
}
