package com.example.tutorme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_swipe.*

class SwipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)

        recViewUserList.layoutManager = LinearLayoutManager(this)
        recViewUserList.adapter = UserListAdapter()
    }
}
