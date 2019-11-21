package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.tutorme.databinding.ActivitySettingsBinding
import com.example.tutorme.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var curUser: Student

    companion object{
        const val DEFAULT_PROFILE_PICTURE = "https://firebasestorage.googleapis.com/v0/b/tutorme-" +
                "backend.appspot.com/o/images%2F44612a04-e07f-408b-a377-572670c82a33?alt=media&token" +
                "=8515b3e0-d428-406e-a0dd-8324ae493c0c"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val user = settingsViewModel.currentUser
        if (user == null) {
            val databaseUserEntry = FirebaseFirestore.getInstance().collection("students").
                document(FirebaseAuth.getInstance().currentUser!!.uid)
            databaseUserEntry.get().addOnSuccessListener {
                settingsViewModel.currentUser = it.toObject(Student::class.java)!!
                loadTextAndPicture(it.toObject(Student::class.java)!!)
            }
        } else {
            loadTextAndPicture(user)
        }


//        val db = FirebaseFirestore.getInstance()
//        val student =
//            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
//        var oldSettings: Student?
//        student.get().addOnSuccessListener { documentSnapshot ->
//            oldSettings = documentSnapshot.toObject(Student::class.java)
//
//            binding.settingsEmail.text = FirebaseAuth.getInstance().currentUser?.email
//            binding.settingsFirstName.text = oldSettings?.first_name
//            binding.settingsLastName.text = oldSettings?.last_name
//            binding.settingsProfilePictureUrl.text = oldSettings?.profile_picture_url
//            binding.settingsSchool.text = oldSettings?.school
//        }



        binding.settingsEditButton.setOnClickListener {
            val intent = Intent(this, EditSettingsActivity::class.java)
            startActivity(intent)
        }

        binding.deleteAccButton.setOnClickListener{
            FirebaseFirestore.getInstance()
                .collection("students")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .delete()
                .addOnSuccessListener { Log.d("DELETE", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("DEBUG", "Error deleting document", e) }
            val intent = Intent(this, EditSettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadTextAndPicture(user: Student){
        binding.settingsEmail.text = user.email
        binding.settingsFirstName.text = user.first_name
        binding.settingsLastName.text = user.last_name
        binding.settingsProfilePictureUrl.text = user.profile_picture_url
        binding.settingsSchool.text = user.school

        if(user?.profile_picture_url == null || user.profile_picture_url!!.isEmpty()){
            Glide.with(this).load(DEFAULT_PROFILE_PICTURE).into(profile_imageview_settings)
        }else{
            Glide.with(this).load(user.profile_picture_url).into(profile_imageview_settings)
        }
    }
}
