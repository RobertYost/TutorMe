package com.example.tutorme

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.drm.DrmStore
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorme.ChatListActivity
import com.example.tutorme.R
import com.example.tutorme.SettingsActivity
import com.example.tutorme.databinding.ActivitySchoolListBinding
import com.example.tutorme.databinding.ActivitySwipeBinding
import com.example.tutorme.models.Student
import com.example.tutorme.models.University
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_swipe.*
import java.lang.Math.PI
import java.math.BigDecimal

private const val TAG = "SchoolListActivity"

class SchoolListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySchoolListBinding
    private var suggest: Double = 50000.0
    private var REQUEST_LOCATION: Int = 2
    private lateinit var coordinates: Location
    private lateinit var located: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var closest = "null"

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permissionName), permissionRequestCode
        )
    }

    private fun showExplanation(
        title: String,
        message: String,
        permission: String,
        permissionRequestCode: Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, id ->
                requestPermission(
                    permission,
                    permissionRequestCode
                )
            }
        builder.create().show()
    }

    @SuppressLint("SetTextI18n")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
                locationAcquiredAction()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun latLonInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371; // Radius of the earth in km
        val dLat = deg2rad(lat2 - lat1);
        val dLon = deg2rad(lon2 - lon1);
        val a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2)
        ;
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        val d = R * c; // Distance in km
        return d;
    }

    private fun deg2rad(deg: Double): Double {
        return (deg * (PI / 180))
    }

    private fun locationAcquiredAction() {
        if (located == "false") {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        coordinates = location
                        located = "true"
                    }
                    val db = FirebaseFirestore.getInstance()
                    val newQuery = db.collection("universities")
                    newQuery.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if (querySnapshot != null) {
                            for (document in querySnapshot.documents) {
                                if (closest == "null"){
                                    closest = document.data?.get("name").toString()
                                    val temp: GeoPoint = document.data?.get("location") as GeoPoint
                                    val dist = latLonInKm(
                                        coordinates.latitude,
                                        coordinates.longitude,
                                        temp.latitude,
                                        temp.longitude
                                    )
                                    suggest = dist
                                }
                                val temp: GeoPoint = document.data?.get("location") as GeoPoint
                                val dist = latLonInKm(
                                    coordinates.latitude,
                                    coordinates.longitude,
                                    temp.latitude,
                                    temp.longitude
                                )
                                if (suggest > dist) {
                                    suggest = dist
                                    closest = document.data?.get("name").toString()
                                }
                            }
                        }
                        binding.suggestedSchool.text = closest
                    }
                }
        } else {
            val db = FirebaseFirestore.getInstance()
            val newQuery = db.collection("universities")
            newQuery.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                        if (closest == "null"){
                            closest = document.data?.get("name").toString()
                            val temp: GeoPoint = document.data?.get("location") as GeoPoint
                            val dist = latLonInKm(
                                coordinates.latitude,
                                coordinates.longitude,
                                temp.latitude,
                                temp.longitude
                            )
                            suggest = dist
                        }
                        val temp: GeoPoint = document.data?.get("location") as GeoPoint
                        val dist = latLonInKm(
                            coordinates.latitude,
                            coordinates.longitude,
                            temp.latitude,
                            temp.longitude
                        )
                        if (suggest > dist) {
                            suggest = dist
                            closest = document.data?.get("name").toString()
                        }
                    }
                }
                binding.suggestedSchool.text = closest
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchoolListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recViewSchoolList.layoutManager = LinearLayoutManager(this)

        val query = FirebaseFirestore.getInstance().collection("universities")
        located = "false"

        if (checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Check Permissions Now
            val REQUEST_LOCATION = 2

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                showExplanation(
                    "Permission needed",
                    "Location access needed to approximate university's location",
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    REQUEST_LOCATION
                )
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION
                )
            }
        } else {
            // permission has been granted, continue as usual
            locationAcquiredAction()
        }

        binding.selectSuggestedBtn.setOnClickListener {
            if (suggest == 50000.0) {
                Toast.makeText(
                    this, "No universities to suggest for you." +
                            " Please add yours", Toast.LENGTH_LONG
                ).show()
                locationAcquiredAction()
            } else {
                val intent = Intent(this, EditSettingsActivity::class.java)
                intent.putExtra("school", binding.suggestedSchool.text)
                startActivity(intent)
            }
        }

        binding.addNewSchool.setOnClickListener {
            val intent = Intent(this, AddSchoolActivity::class.java)
            startActivity(intent)
        }

        val builder = FirestoreRecyclerOptions.Builder<University>()
            .setQuery(query) { snapshot ->
                snapshot.toObject(University::class.java)!!.also {
                    it.short_name = snapshot["short_name"].toString()
                    it.name = snapshot["name"].toString()
                }
            }.setLifecycleOwner(this)

        val options = builder.build()
        val adapter = SchoolListAdapter(options)
        binding.recViewSchoolList.adapter = adapter
    }
}

