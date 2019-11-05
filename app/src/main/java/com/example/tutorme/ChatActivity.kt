package com.example.tutorme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tutorme.models.ChatMessage
import com.example.tutorme.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.from_chat_row.view.*
import kotlinx.android.synthetic.main.to_chat_row.view.*

private const val TAG = "chat"

class ChatActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()
    var toStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Chat"

        recycleview_chat.adapter = adapter

        listenForMessages()

        send_button_chat.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().currentUser?.uid
        //TODO( get toId for other user)
        val toId = "Replace this with the other users uid"
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null){
                    if(chatMessage.fromId == FirebaseAuth.getInstance().currentUser?.uid){
                        adapter.add(ChatFromItem(chatMessage.text))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    private fun performSendMessage(){
        val fromId = FirebaseAuth.getInstance().currentUser?.uid
        //TODO( get toId for other user)
        val toId = "Replace this with the other users uid"
        val textMessage = edittext_chat.text.toString()
        val fromRef = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(fromRef.key!!, textMessage, fromId!!, toId, System.currentTimeMillis()/1000)

        toRef.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Message saved")
        }.addOnFailureListener{
            Log.d(TAG, "Failed to save message")
        }

        fromRef.setValue(chatMessage)

    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.from_textview_chat.text = text
    }

    override fun getLayout(): Int {
        return R.layout.from_chat_row
    }
}

class ChatToItem(val text: String): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.to_textview_chat.text = text
    }

    override fun getLayout(): Int {
        return R.layout.to_chat_row
    }
}