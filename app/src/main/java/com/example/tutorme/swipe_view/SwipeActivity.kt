package com.example.tutorme.swipe_view

import android.content.Intent
import android.drm.DrmStore
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorme.ChatListActivity
import com.example.tutorme.R
import com.example.tutorme.SettingsActivity
import com.example.tutorme.databinding.ActivitySwipeBinding
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_swipe.*

private const val TAG = "SwipeActivity"

class SwipeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Handles the item selection in the drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_add_class -> {
                val intent = Intent(this, SettingsActivity::class.java)
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
            else -> Log.d("DEBUG", "Odd interaction with the navigation drawer...")
        }
        return true
    }

    private lateinit var binding: ActivitySwipeBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tbar = binding.toolbar
        setSupportActionBar(tbar)
        binding.recViewUserList.layoutManager = LinearLayoutManager(this)

        // Handling setup for drawer and navigation view
        drawer = binding.drawerLayout
        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.settingBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

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

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

