package com.example.tutorme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutorme.databinding.ActivityChatListBinding
import com.example.tutorme.models.ChatLog
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat_list.*


class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycleviewChatList.layoutManager = LinearLayoutManager(this)

        val query = FirebaseFirestore.getInstance().collection("chat_logs").whereEqualTo("user_one", FirebaseAuth.getInstance().currentUser!!.uid)
        println(query.toString())

        val builder = FirestoreRecyclerOptions.Builder<ChatLog>()
            .setQuery(query) { snapshot ->
                snapshot.toObject(ChatLog::class.java)!!.also {
                    it.user_two = snapshot["user_two"].toString()
                }
            }.setLifecycleOwner(this)

        val options = builder.build()
        val adapter = ChatListAdapter(options)
        recycleview_chat_list.adapter = adapter
    }
}