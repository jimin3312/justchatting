package com.example.justchatting.ui.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_item.view.*
import kotlinx.android.synthetic.main.fragment_friend_my_item.view.*

class FriendAdapter(var UserList : MutableLiveData<ArrayList<User>>): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View?
        return when (viewType) {
            0 -> {
               view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_my_item,parent, false)
               myViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_item, parent, false)
                userViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        if(UserList.value == null)
            return 0
        else
            return UserList.value!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(position)
        {
            0->{

                holder.itemView.friend_my_textview_username.text = UserList.value!![position].username
                val targetImageView = holder.itemView.friend_my_imageview_profile_image ?: return
                Picasso.get().load(UserList.value!![position].profileImageUrl)
                    .placeholder(R.drawable.person)
                    .into(targetImageView)
            }
            else->{
                holder.itemView.friend_textview_username.text = UserList.value!![position].username
                val targetImageView = holder.itemView.friend_imageview_profile_image ?: return
                Picasso.get().load(UserList.value!![position].profileImageUrl)
                    .placeholder(R.drawable.person)
                    .into(targetImageView)
            }
        }
    }
    inner class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}