package com.example.tutorme.swipe_view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorme.databinding.ActivitySwipeBinding
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.example.tutorme.*
import com.example.tutorme.models.Class
import com.firebase.ui.auth.AuthUI
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*


private const val TAG = "SwipeActivity"

class SwipeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Handles the item selection in the drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add_class -> {
                val intent = Intent(this, AddClassActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_chat -> {
                val intent = Intent(this, ChatListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_sign_out -> {
                val intent = Intent(this, MainActivity::class.java)
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        // user is now signed out
                        startActivity(intent)
                        finish()
                    }
            }
            else -> Log.d("DEBUG", "Odd interaction with the navigation drawer...")
        }
        return true
    }

    private lateinit var binding: ActivitySwipeBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        binding.recViewClassList.layoutManager = LinearLayoutManager(this)

        // Handling setup for drawer and navigation view
        drawer = binding.drawerLayout
        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        FirebaseAuth.getInstance().uid?.let { it ->
            FirebaseFirestore.getInstance()
                .collection("students").document(it).get()
                .addOnSuccessListener {
                    val user = it.toObject(Student::class.java)
                    navigationView.draw_full_name.text =
                        "${user?.first_name} " +
                                "${user?.last_name}"
                    println("THING1 ${user?.first_name} ${user?.last_name}")
                    navigationView.draw_email.text =
                        FirebaseAuth.getInstance().currentUser?.email
                    var profilePic = user?.profile_picture_url
                    if (user?.profile_picture_url == null || user.profile_picture_url!!.isEmpty()) {
                        profilePic = SettingsActivity.DEFUALT_PROFILE_PICTURE
                    }
                    //Picasso.get().load(profilePic).into(nav_profile_pic)
                    Glide.with(this).load(profilePic).into(nav_profile_pic)
                }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // TODO: Change this query to only match with the tutors of the student's classes
        FirebaseAuth.getInstance().uid?.let { it ->
            FirebaseFirestore.getInstance()
                .collection("students").document(it).get()
                .addOnSuccessListener { outerIt ->
                    val user = outerIt.toObject(Student::class.java)

                    // Making a query to discover which classes we will seek tutors for
                    val usersClasses = FirebaseFirestore.getInstance()
                        .collection("classes").whereEqualTo("student_id", user?.id)

                    val builder = FirestoreRecyclerOptions.Builder<Class>()
                        .setQuery(usersClasses, Class::class.java).setLifecycleOwner(this)

                    //) { snapshot ->
                    //                            snapshot.toObject(Class::class.java)!!.also {
                    //                                it.student_id = snapshot.id
                    //                                it.dpt_code = snapshot["dpt_code"].toString()
                    //                                it.class_code = snapshot["class_code"].toString()
                    //                            }
                    //                        }

                    val options = builder.build()
                    val adapter = ClassListAdapter(options)
                    binding.recViewClassList.adapter = adapter
                }
        }

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

