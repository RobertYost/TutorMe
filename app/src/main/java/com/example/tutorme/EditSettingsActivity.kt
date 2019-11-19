package com.example.tutorme

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorme.databinding.ActivityEditSettingsBinding
import com.example.tutorme.models.Student
import com.example.tutorme.swipe_view.SwipeActivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_settings.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

private const val TAG = "editsettings"
class EditSettingsActivity : AppCompatActivity() {

    // Using view-binding from arch-components (requires Android Studio 3.6 Canary 11+)
    private lateinit var binding: ActivityEditSettingsBinding
    private lateinit var theSchool: String
    private lateinit var db: FirebaseFirestore
    private var internetDisposable: Disposable? = null
    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("EditSettingsActivity", "Created EditSettingsActivity")
        super.onCreate(savedInstanceState)
        binding = ActivityEditSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        theSchool = "default"

        val thisIntent = intent
        if (thisIntent.getStringExtra("school") != null) {
            theSchool = thisIntent.getStringExtra("school")!!
            binding.editSettingsSchool.text = theSchool
        }

        val student =
            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
        var oldSettings: Student?
//        var userExists = false
        student.get().addOnSuccessListener {
            oldSettings = it.toObject(Student::class.java)
            binding.editSettingsEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
            binding.editSettingsFirstName.setText(oldSettings?.first_name)
            binding.editSettingsLastName.setText(oldSettings?.last_name)
            if(oldSettings != null){
                theSchool = oldSettings?.school.toString()
                binding.editSettingsSchool.text = oldSettings?.school
                binding.editSchoolBtn.isEnabled = false
            }
        }

        binding.selectPhotoEditSettings.setOnClickListener {
            Log.d(TAG, "try to select photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        binding.editSchoolBtn.setOnClickListener {
                // Redirects back to the tutor list page after saving
                val intent = Intent(this, SchoolListActivity::class.java)
                startActivity(intent)
        }

        binding.editSettingsSaveButton.setOnClickListener {
            uploadImageToFirebaseStorage()
            saveUserToFireStore(selectedPhotoUri.toString())
            //TODO: Update vs. Create (Currently works fine as is, maybe change for NFR Checkpoint)
//            if(!userExists){
//                Log.d("DEBUG", "Should have created user")
//                db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid).set(settings).addOnSuccessListener { Log.d("Document", "Successfully created user") }
//            } else {
//                db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid).set(settings, SetOptions.merge())
//            }
        }
    }

    override fun onPause() {
        super.onPause()
        safelyDispose(internetDisposable)
    }

    private fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun saveUserToFireStore(profilePictureUrl: String){
        // If the school hasn't been selected or info is missing, refuse the save
        if(theSchool == "default" || binding.editSettingsFirstName.length() == 0){
            Toast.makeText(this, "Please make sure to select your school and " +
                    "enter your name!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show()

            // Prepares the settings based on the fields
            val settings = hashMapOf(
                "id" to FirebaseAuth.getInstance().currentUser!!.uid,
                "first_name" to binding.editSettingsFirstName.text.toString(),
                "last_name" to binding.editSettingsLastName.text.toString(),
                "profile_picture_url" to profilePictureUrl,
                "school" to binding.editSettingsSchool.text.toString()
            )

            // Adds or updates the document to the students collection based on the login email used
            db.collection("students").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .set(settings)

            // Redirects back to the tutor list page after saving
            val intent = Intent(this, SwipeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val imageFileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$imageFileName")
        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                saveUserToFireStore(it.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            bitmap = selectedPhotoUri?.let { rotateImageIfNeeded(bitmap, it) }
            profilePicImgView.setImageBitmap(bitmap)

            select_photo_edit_settings.alpha = 0f
        }
    }
    override fun onResume() {
        val thisIntent = intent
        if (thisIntent.getStringExtra("school") != null) {
            theSchool = thisIntent.getStringExtra("school")!!
            binding.editSettingsSchool.text = theSchool
        }
        super.onResume()


        internetDisposable = ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { connectivity ->
                binding.editSchoolBtn.isEnabled = connectivity.available()
                binding.editSettingsSchool.isEnabled = connectivity.available()
                binding.editSettingsFirstName.isEnabled = connectivity.available()
                binding.editSettingsLastName.isEnabled = connectivity.available()
                binding.editSettingsEmail.isEnabled = connectivity.available()
            }

    }

    private fun rotateImageIfNeeded(image: Bitmap, imageUri: Uri): Bitmap {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return image
        val input = contentResolver.openInputStream(imageUri)
        val exifInterface = ExifInterface(input!!)

        return when(exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)){
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(image, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(image, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(image, 270f)
            else -> image
        }
    }

    private fun rotateImage(image: Bitmap, rotationDegrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees)
        val rotatedImage = Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
        image.recycle()
        return rotatedImage
    }
}
