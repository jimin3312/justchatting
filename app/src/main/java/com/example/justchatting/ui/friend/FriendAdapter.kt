package com.example.justchatting.ui.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_friend.*
import kotlinx.android.synthetic.main.fragment_friend_item.view.*
import org.koin.ext.getScopeId

class FriendAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object{
        val VIEW_TYPE_MY = 0
        val VIEW_TYPE_USERS = 1
    }
    private var mFriendList: ArrayList<User>? = null

    fun setUsers(friendList: ArrayList<User>) {
        mFriendList = friendList
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==0)
            VIEW_TYPE_MY
        else
            VIEW_TYPE_USERS
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        if(viewType == VIEW_TYPE_MY)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_item,parent,false)
//        return UserViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        holder.bind(mFriendList?.get(position))
//    }

    override fun getItemCount(): Int {
        if(mFriendList == null)
            return 0
        return mFriendList!!.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_MY) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_my_item, parent,false)
            MyViewHolder(view)
        } else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_item, parent, false)
            UserViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                holder.bind(mFriendList!![position])
            }
            is UserViewHolder -> {
                holder.bind(mFriendList!![position])
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var user : User? = null
        private val profileImg = itemView.findViewById<CircleImageView>(R.id.friend_my_imageview_profile_image)
        private val username=itemView.findViewById<TextView>(R.id.friend_my_textview_username)

        fun bind(user : User?)
        {
            if(user == null) return
            this.user = user
            username.text = user.username
            if(user.profileImageUrl!!.isEmpty())
                profileImg.setImageResource(R.drawable.person)
            else
                Picasso.get().load(user.profileImageUrl).placeholder(R.drawable.person).into(profileImg)
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var user : User? = null
        private val profileImg = itemView.findViewById<CircleImageView>(R.id.friend_imageview_profile_image)
        private val username =itemView.findViewById<TextView>(R.id.friend_textview_username)

        fun bind(user : User?)
        {
            if(user == null) return
            this.user = user
            username.text = user.username
            if(user.profileImageUrl!!.isEmpty())
                profileImg.setImageResource(R.drawable.person)
            else
                Picasso.get().load(user.profileImageUrl).placeholder(R.drawable.person).into(profileImg)
        }
    }


}