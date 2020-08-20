package com.example.justchatting.ui.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_item.view.*

class FriendAdapter(): PagedListAdapter<User, UserViewHolder>(DIFF_CALLBACK)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user : User? = getItem(position)
        holder.bindTo(user)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<User>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldUser : User,
                                         newUser : User) = oldUser.uid == newUser.uid

            override fun areContentsTheSame(oldUser: User,
                                            newUser: User) = oldUser == newUser
        }
    }
}