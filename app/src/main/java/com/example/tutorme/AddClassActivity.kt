package com.example.tutorme

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.tutorme.databinding.ActivityAddSchoolBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.tutorme.databinding.ActivityAddClassBinding
import com.example.tutorme.models.Class
import com.example.tutorme.swipe_view.SwipeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import kotlinx.android.synthetic.main.activity_add_class.view.*


class AddClassActivity : AppCompatActivity() {

    // Using view-binding from arch-components (requires Android Studio 3.6 Canary 11+)
    private lateinit var binding: ActivityAddClassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        binding.addClassRadioGroup.add_class_tutor.setOnClickListener {
            binding.addClassTutorPricePlaceholder.isEnabled = true
        }

        binding.addClassRadioGroup.add_class_student.setOnClickListener {
            binding.addClassTutorPricePlaceholder.isEnabled = false
        }

        binding.addClassAddButton.setOnClickListener {
//TODO: things to fix in here
            // If the school hasn't been selected or info is missing, refuse the save
            if(binding.addClassMajorPlaceholder.text.isEmpty() || binding.addClassCourseNumber.text.isEmpty() || binding.addClassRadioGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "Please make sure to fill out all the fields!",
                    Toast.LENGTH_SHORT).show()
            } else {
                val doc = db.collection("students").document(FirebaseAuth
                    .getInstance().currentUser!!.uid)
                var user_school = "temp"
                doc.addSnapshotListener { documentSnapshot, _ ->
                    user_school = documentSnapshot?.get("universities") as String
                }
                // Prepares the settings based on the fields
                val settings = hashMapOf(
                    "student_id" to FirebaseAuth.getInstance().currentUser!!.uid,
//                    "is_tutor" to binding.addClassTutor.isChecked,
//                    "tutor_price" to binding.addClassTutorPricePlaceholder,
//                    "school" to user_school,
                    "dpt_code" to binding.addClassMajorPlaceholder.text.toString(),
                    "class_code" to binding.addClassCourseNumber.text.toString()
                )

                // Adds or updates the document to the students collection based on the login email used
                db.collection("classes").document().set(settings)

                // Redirects back to the tutor list page after saving
                val intent = Intent(this, SwipeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
