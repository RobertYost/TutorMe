package com.example.tutorme.swipe_view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorme.R
import com.example.tutorme.SettingsActivity
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_row.view.*


class UserListAdapter(options: FirestoreRecyclerOptions<Student>) :
    FirestoreRecyclerAdapter<Student, UserListAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_row, parent, false)

        return ViewHolder(cellForRow)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Student) {
        holder.containerView.setOnClickListener {

            //            TODO: Make this redirect to the tutor's profile page
            val intent = Intent(holder.containerView.context, SettingsActivity::class.java)
            intent.putExtra("TUTOR_UID", item.id)
            holder.containerView.context.startActivity(intent)
        }

        holder.containerView.apply {
            nameTextView.text = "${item.first_name} ${item.last_name}"
            schoolTextView.text = item.school
        }
    }



    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
