package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.example.tutorme.databinding.ActivityAddClassBinding
import com.example.tutorme.models.Student
import com.example.tutorme.swipe_view.SwipeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_add_class.view.*
import java.util.*
import kotlin.collections.HashMap


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
            // If the school hasn't been selected or info is missing, refuse the save
            if(binding.addClassMajorPlaceholder.text.isEmpty() || binding.addClassCourseNumber.text.isEmpty() || binding.addClassRadioGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "Please make sure to fill out all the fields!",
                    Toast.LENGTH_SHORT).show()
            } else {

                val doc = db.collection("students").document(FirebaseAuth
                    .getInstance().currentUser!!.uid)
                var userSchool: String
                doc.get().addOnSuccessListener {
                    userSchool = it.toObject(Student::class.java)?.school.toString()

                    // Prepares the settings based on the fields
                    val settings = hashMapOf(
                        "student_id" to FirebaseAuth.getInstance().currentUser!!.uid,
                        "is_tutor" to binding.addClassTutor.isChecked,
                        "tutor_price" to binding.addClassTutorPricePlaceholder.text.toString(),
                        "school" to userSchool,
                        "dpt_code" to binding.addClassMajorPlaceholder.text.toString().toUpperCase(
                            Locale.ROOT),
                        "class_code" to binding.addClassCourseNumber.text.toString()
                    )

                    // Checks to see if the student is already enrolled in that class
                    val classDoc = db.collection("classes")
                        .whereEqualTo("student_id", settings["student_id"])
                        .whereEqualTo("school", settings["school"])
                        .whereEqualTo("dpt_code", settings["dpt_code"])
                        .whereEqualTo("class_code", settings["class_code"])
                    classDoc.addSnapshotListener { querySnapshot, _ ->
                        checkDuplicate(querySnapshot, db, settings)
                    }



                }
            }
        }
    }

    private fun checkDuplicate(
        querySnapshot: QuerySnapshot?,
        db: FirebaseFirestore,
        settings: HashMap<String, Any>
    ) {
        if (querySnapshot!!.isEmpty) {
            // Adds or updates the document to the students collection based on the login email used
            db.collection("classes").document().set(settings)

            // Redirects back to the tutor list page after saving
            val intent = Intent(this, SwipeActivity::class.java)
            startActivity(intent)
        } else {
            binding.addClassError.setText(R.string.add_class_duplicate)
        }
    }
}
