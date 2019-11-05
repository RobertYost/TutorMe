package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorme.databinding.ActivityEditSettingsBinding
import com.example.tutorme.models.Student
import com.example.tutorme.swipe_view.SwipeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditSettingsActivity : AppCompatActivity() {

    // Using view-binding from arch-components (requires Android Studio 3.6 Canary 11+)
    private lateinit var binding: ActivityEditSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("EditSettingsActivity", "Created EditSettingsActivity")
        super.onCreate(savedInstanceState)
        binding = ActivityEditSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        val student =
            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
        var oldSettings: Student?
//        var userExists = false
        student.get().addOnSuccessListener {
            oldSettings = it.toObject(Student::class.java)
            binding.editSettingsFirstName.setText(oldSettings?.first_name)
            binding.editSettingsLastName.setText(oldSettings?.last_name)
            binding.editSettingsProfilePic.setText(oldSettings?.profile_picture_url)
            binding.editSettingsSchool.setText(oldSettings?.school)
        }


        binding.editSettingsSaveButton.setOnClickListener {
            // Prepares the settings based on the fields
            val settings = hashMapOf(
                "id" to FirebaseAuth.getInstance().currentUser!!.uid,
                "first_name" to binding.editSettingsFirstName.text.toString(),
                "last_name" to binding.editSettingsLastName.text.toString(),
                "password" to binding.editSettingsPassword.text.toString(),
                "profile_picture_url" to binding.editSettingsProfilePic.text.toString(),
                "school" to binding.editSettingsSchool.text.toString()
            )

            // Adds or updates the document to the students collection based on the login email used
            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .set(settings)

            // Redirects back to the tutor list page after saving
            val intent = Intent(this, SwipeActivity::class.java)
            startActivity(intent)

            //TODO: Update vs. Create (Currently works fine as is, maybe change for NFR Checkpoint)
//            if(!userExists){
//                Log.d("DEBUG", "Should have created user")
//                db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid).set(settings).addOnSuccessListener { Log.d("Document", "Successfully created user") }
//            } else {
//                db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid).set(settings, SetOptions.merge())
//            }

        }
    }
}
