package com.example.tutorme.swipe_view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorme.SettingsActivity
import com.example.tutorme.databinding.ActivitySwipeBinding
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "SwipeActivity"

class SwipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recViewUserList.layoutManager = LinearLayoutManager(this)

        binding.settingBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        Log.d(TAG, FirebaseAuth.getInstance().currentUser?.uid)

        val query = FirebaseFirestore.getInstance().collection("students")

        val builder = FirestoreRecyclerOptions.Builder<Student>()
            .setQuery(query) { snapshot ->
                snapshot.toObject(Student::class.java)!!.also {
                    it.id = snapshot.id
                    it.school = snapshot["school"].toString()
                }
            }.setLifecycleOwner(this)

        val options = builder.build()
        val adapter = UserListAdapter(options)
        binding.recViewUserList.adapter = adapter
    }
}
