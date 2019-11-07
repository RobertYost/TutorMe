package com.example.tutorme.swipe_view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorme.ChatActivity
import com.example.tutorme.ChatListActivity
import com.example.tutorme.R
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
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

            //Don't allow user to create a chat with themselves
            if(item.id == FirebaseAuth.getInstance().uid) return@setOnClickListener
            val intent = Intent(holder.containerView.context, ChatActivity::class.java)
            intent.putExtra(ChatListActivity.USER_KEY, item)
            holder.containerView.context.startActivity(intent)
        }

        holder.containerView.apply {
            primaryTextView.text = "${item.first_name} ${item.last_name}"
            secondaryTextView.text = item.school
        }
    }



    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
