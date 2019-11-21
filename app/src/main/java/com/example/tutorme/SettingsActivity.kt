package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tutorme.databinding.ActivitySettingsBinding
import com.example.tutorme.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var curUser: Student

    companion object {
        const val DEFUALT_PROFILE_PICTURE = "https://firebasestorage.googleapis.com/v0/b/tutorme-" +
                "backend.appspot.com/o/images%2F44612a04-e07f-408b-a377-572670c82a33?alt=media&token" +
                "=8515b3e0-d428-406e-a0dd-8324ae493c0c"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        if (savedInstanceState == null) {
            val extras = this.intent.extras
            curUser = extras!!.get("cur_user") as Student
            Log.d("DEBUG", curUser.toString())
        }

        binding.settingsEmail.text = FirebaseAuth.getInstance().currentUser?.email
        binding.settingsFirstName.text = curUser.first_name
        binding.settingsLastName.text = curUser.last_name
        binding.settingsProfilePictureUrl.text = curUser.profile_picture_url
        binding.settingsSchool.text = curUser.school

        if (curUser.profile_picture_url == null || curUser.profile_picture_url!!.isEmpty()) {
            Glide.with(this).load(DEFUALT_PROFILE_PICTURE).into(profile_imageview_settings)
        } else {
            Glide.with(this).load(curUser.profile_picture_url).into(profile_imageview_settings)
        }

        binding.settingsEditButton.setOnClickListener {
            val intent = Intent(this, EditSettingsActivity::class.java)
            startActivity(intent)
        }

        binding.deleteAccButton.setOnClickListener {
            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .delete()
                .addOnSuccessListener { Log.d("DELETE", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("DEBUG", "Error deleting document", e) }
            val intent = Intent(this, EditSettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
