package com.example.tutorme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_row.view.*

class UserListAdapter : RecyclerView.Adapter<UserCellViewHolder>() {

    private val users = listOf("Bobby", "Ryan", "Andrew")

    // numberOfItems
    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCellViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.user_row, parent, false)
        return UserCellViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: UserCellViewHolder, position: Int) {
        val user = users[position]
        holder.itemView.textViewName.text = user
    }
}

class UserCellViewHolder(v: View) : RecyclerView.ViewHolder(v)