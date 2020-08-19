package com.example.justchatting.ui.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_item.view.*

class FriendAdapter(var UserList : LiveData<List<User>>): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_item, parent, false)
        return userViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(UserList.value == null)
            return 0
        else
            return UserList.value!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.friend_textview_username.text = UserList.value!![position].username
        val targetImageView = holder.itemView.friend_imageview_profile_image ?: return

        Picasso.get().load(UserList.value!![position].profileImageUrl)
            .placeholder(R.drawable.person)
            .into(targetImageView)
    }
    inner class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}