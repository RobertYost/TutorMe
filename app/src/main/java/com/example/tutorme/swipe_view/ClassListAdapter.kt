package com.example.tutorme.swipe_view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorme.models.Class
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.extensions.LayoutContainer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.user_row.view.*
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.content.Context
import com.example.tutorme.MainActivity
import kotlin.system.exitProcess


class ClassListAdapter(options: FirestoreRecyclerOptions<Class>) :
    FirestoreRecyclerAdapter<Class, ClassListAdapter.ViewHolder>(options) {

    private lateinit var toDelete: Class
    private  lateinit var vg: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(com.example.tutorme.R.layout.user_row, parent, false)
        vg = parent
        return ViewHolder(cellForRow)
    }

    private fun onButtonShowPopupWindowClick(view: View) {

        // inflate the layout of the popup window
        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater!!.inflate(com.example.tutorme.R.layout.delete_class_window, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // dismiss the popup window when touched
        popupView.setOnTouchListener(View.OnTouchListener { v, event ->
            popupWindow.dismiss()
            var docToDelete = "default"
            FirebaseFirestore.getInstance().collection("classes")
                .whereEqualTo("school", toDelete.school)
                .whereEqualTo("dpt_code", toDelete.dpt_code)
                .whereEqualTo("class_code", toDelete.class_code)
                .whereEqualTo("student_id", toDelete.student_id)
                .addSnapshotListener { querySnapshot, _ ->
                    if (querySnapshot != null) {
                        for (doc in querySnapshot.documents){
                            docToDelete = doc.id
                        }
                        FirebaseFirestore.getInstance().collection("classes")
                            .document(docToDelete).delete().addOnSuccessListener {
                                Toast.makeText(vg.context, "Successfully deleted the class", Toast.LENGTH_LONG).show()
                                val mStartActivity = Intent(vg.context, MainActivity::class.java)
                                val mPendingIntentId = 123456
                                val mPendingIntent = PendingIntent.getActivity(
                                    vg.context,
                                    mPendingIntentId,
                                    mStartActivity,
                                    PendingIntent.FLAG_CANCEL_CURRENT
                                )
                                val mgr =
                                    vg.context.getSystemService(ALARM_SERVICE) as AlarmManager
                                mgr.set(
                                    AlarmManager.RTC,
                                    System.currentTimeMillis() + 100,
                                    mPendingIntent
                                )
                                exitProcess(0)
                            }
                    }
                }
            true
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Class) {
        holder.containerView.setOnClickListener {
            if (item.is_tutor == true) {
                toDelete = item
                onButtonShowPopupWindowClick(holder.containerView)
            } else {
                val intent = Intent(holder.containerView.context, UserListActivity::class.java)
                intent.putExtra("WHICH_CLASS", item)
                holder.containerView.context.startActivity(intent)
            }

        }

        holder.containerView.apply {
            primaryTextView.text = "${item.dpt_code} " +
                    item.class_code
            if (item.is_tutor == true) {
                secondaryTextView.text = context.getString(com.example.tutorme.R.string.class_list_tutor)
            } else {
                secondaryTextView.text = context.getString(com.example.tutorme.R.string.class_list_student)
            }
        }
    }

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
