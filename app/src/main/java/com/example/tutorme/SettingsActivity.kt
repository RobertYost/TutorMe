package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorme.databinding.ActivitySettingsBinding
import com.example.tutorme.models.Student
import com.example.tutorme.swipe_view.SwipeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        val student =
            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
        var oldSettings: Student?
        student.get().addOnSuccessListener { documentSnapshot ->
            oldSettings = documentSnapshot.toObject(Student::class.java)

            binding.settingsEmail?.text = FirebaseAuth.getInstance().currentUser?.email
            binding.settingsFirstName.text = oldSettings?.first_name
            binding.settingsLastName.text = oldSettings?.last_name
            binding.settingsProfilePictureUrl.text = oldSettings?.profile_picture_url
            binding.settingsSchool.text = oldSettings?.school
        }

        binding.settingsEditButton.setOnClickListener{
            val intent = Intent(this, EditSettingsActivity::class.java)
            startActivity(intent)
        }

        binding.deleteAccButton.setOnClickListener{
            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .delete()
                .addOnSuccessListener { Log.d("DELETE", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("DEBUG", "Error deleting document", e) }
            val intent = Intent(this, SwipeActivity::class.java)
            startActivity(intent)
        }
    }
}
