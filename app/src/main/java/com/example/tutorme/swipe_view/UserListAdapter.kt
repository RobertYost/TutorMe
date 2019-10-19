package com.example.tutorme.swipe_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorme.R
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Student) {

        holder.containerView.apply {
            nameTextView.text = "${item.first_name} ${item.last_name}"
            schoolTextView.text = item.school
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
