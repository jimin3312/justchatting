package com.example.justchatting.ui.chattingRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.databinding.FriendItemBinding
import com.squareup.picasso.Picasso

class FriendsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mFriendList: ArrayList<UserModel>? = null

    fun setUsers(friendList: ArrayList<UserModel>) {
        mFriendList = friendList
    }

    override fun getItemCount(): Int {
        if(mFriendList == null)
            return 0
        return mFriendList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(FriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserViewHolder).bind(mFriendList!![position])
    }

    inner class UserViewHolder(binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        var userModel : UserModel? = null
        private val profileImg = binding.friendImageviewProfileImage
        private val username = binding.friendTextviewUsername
        fun bind(userModel : UserModel?)
        {
            if(userModel == null) return
            this.userModel = userModel
            username.text = userModel.username
            if(userModel.profileImageUrl!!.isEmpty())
                profileImg.setImageResource(R.drawable.person)
            else
                Picasso.get().load(userModel.profileImageUrl).placeholder(R.drawable.person).into(profileImg)
        }
    }
}
