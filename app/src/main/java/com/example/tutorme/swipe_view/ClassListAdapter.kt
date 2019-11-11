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
import com.example.tutorme.R.string
import com.example.tutorme.SettingsActivity
import com.example.tutorme.models.Class
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_row.view.*


class ClassListAdapter(options: FirestoreRecyclerOptions<Class>) :
    FirestoreRecyclerAdapter<Class, ClassListAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_row, parent, false)

        return ViewHolder(cellForRow)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Class) {
        holder.containerView.setOnClickListener {
            val intent = Intent(holder.containerView.context, UserListActivity::class.java)
            intent.putExtra("WHICH_CLASS", item)
            holder.containerView.context.startActivity(intent)
        }

        holder.containerView.apply {
            primaryTextView.text = "${item.dpt_code} " +
                    item.class_code
            if (item.is_tutor == true) {
                secondaryTextView.text = context.getString(string.class_list_tutor)
            } else {
                secondaryTextView.text = context.getString(string.class_list_student)
            }
        }
    }

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
