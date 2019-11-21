package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler


class AddClassActivity : AppCompatActivity() {

    // Using view-binding from arch-components (requires Android Studio 3.6 Canary 11+)
    private lateinit var binding: ActivityAddClassBinding
    private lateinit var curUser: Student

    fun invalidClass(mjr: String, crsNum: String, radChecked: Number): Boolean {
        return (mjr.isEmpty()
                || crsNum.isEmpty()
                || radChecked == -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        if (savedInstanceState == null) {
            val extras = this.intent.extras
            curUser = extras!!.get("cur_user") as Student
            Log.d("DEBUG", curUser.toString())
        }

        binding.addClassRadioGroup.add_class_tutor.setOnClickListener {
            binding.addClassTutorPricePlaceholder.isEnabled = true
        }

        binding.addClassRadioGroup.add_class_student.setOnClickListener {
            binding.addClassTutorPricePlaceholder.isEnabled = false
        }

        binding.addClassAddButton.setOnClickListener {

            val mjr = binding.addClassMajorPlaceholder.text.toString()
            val crsNum = binding.addClassCourseNumber.text.toString()
            val radChecked = binding.addClassRadioGroup.checkedRadioButtonId
            // If the school hasn't been selected or info is missing, refuse the save
            if(invalidClass(mjr, crsNum, radChecked)){
                Toast.makeText(this, "Please make sure to fill out all the fields!",
                    Toast.LENGTH_SHORT).show()
            } else {
                    // Prepares the settings based on the fields
                    val settings = hashMapOf(
                        "student_id" to FirebaseAuth.getInstance().currentUser!!.uid,
                        "is_tutor" to binding.addClassTutor.isChecked,
                        "tutor_price" to binding.addClassTutorPricePlaceholder.text.toString(),
                        "school" to curUser.school,
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

    private fun checkDuplicate(
        querySnapshot: QuerySnapshot?,
        db: FirebaseFirestore,
        settings: HashMap<String, Any?>
    ) {
        if (querySnapshot!!.isEmpty) {
            // Adds or updates the document to the students collection based on the login email used
            db.collection("classes").document().set(settings)

            // Redirects back to the tutor list page after saving
            val intent = Intent(this, SwipeActivity::class.java)
            intent.putExtra("cur_user", curUser)
            startActivity(intent)
        } else {
            val handler = Handler()
            handler.postDelayed({
                binding.addClassError.setText(R.string.add_class_duplicate) //Add error text
            }, 250) // 250ms delay

        }
    }
}
