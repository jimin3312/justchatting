package com.example.justchatting.ui.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_item,parent,false)
){
    var user : User? = null
    private val profileImg = itemView.findViewById<CircleImageView>(R.id.friend_imageview_profile_image)
    private val username =itemView.findViewById<TextView>(R.id.friend_textview_username)

    fun bindTo(user : User?)
    {
        this.user = user
        username.text = user?.username
        Picasso.get().load(user?.profileImageUrl)
            .placeholder(R.drawable.person)
            .into(profileImg)
    }
}