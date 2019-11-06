package com.example.tutorme

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorme.databinding.ActivityChatListBinding
import com.example.tutorme.models.ChatLog
import com.example.tutorme.models.ChatMessage
import com.example.tutorme.models.Student
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.latest_message_row.view.*


class ChatListActivity : AppCompatActivity() {

    companion object {
        var currentUser: Student? = null
        val USER_KEY = "USER_KEY"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    val latestMessagesMap = HashMap<String, ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        recyclerview_latestmessages.adapter = adapter
        recyclerview_latestmessages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        listenForLatestMessage()

        fetchCurrentUser()
    }

    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessage(){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-message/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }



        })
    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(Student::class.java)
            }

        })
    }

}

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){

    var chatPartnerUser: Student? = null
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.latest_message_textview_latestmessage.text = chatMessage.text
        val chatPartnerId = if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatMessage.toId
        } else {
            chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/user/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(Student::class.java)
                viewHolder.itemView.user_name_textview_latestmessage.text = chatPartnerUser?.first_name
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}