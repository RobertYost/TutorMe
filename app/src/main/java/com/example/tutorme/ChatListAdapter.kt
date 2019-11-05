package com.example.tutorme

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorme.models.ChatLog
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_row.view.*

class ChatListAdapter(options: FirestoreRecyclerOptions<ChatLog>) :
    FirestoreRecyclerAdapter<ChatLog, ChatListAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_row, parent, false)
        return ViewHolder(cellForRow)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: ChatLog) {

        val db = FirebaseFirestore.getInstance()
        Log.d("DEBUGLOG", item.user_one.toString())
        val tutorDoc = db.collection("students").document(item.user_two.toString())
        var str: String

//        TODO: Show list of all chat logs of this user
        Log.d("DEBUGLOG", "${tutorDoc.get()}")
        tutorDoc.get().addOnSuccessListener { documentSnapshot ->
            Log.d("DEBUGLOG", "Doc gotten successfully")
            str = documentSnapshot.getString("first_name").toString() + " " +
                    documentSnapshot.getString("last_name").toString()
            Log.d("DEBUGLOG", str)
            holder.containerView.apply {
                nameTextView.text = str
            }
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
