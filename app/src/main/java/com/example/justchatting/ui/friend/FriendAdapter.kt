package com.example.justchatting.ui.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.Event
import com.example.justchatting.R
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.databinding.FriendItemBinding
import com.example.justchatting.databinding.FriendMyItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class FriendAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_ME = 0
        const val VIEW_TYPE_FRIENDS = 1
    }

    private val uid = FirebaseAuth.getInstance().uid
    private var mFriendList: ArrayList<UserModel>? = null
    var groupMembers = HashMap<String, UserModel?>()

    var itemClick = MutableLiveData<Event<Boolean>>()

    fun setUsers(friendList: ArrayList<UserModel>) {
        mFriendList = friendList
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            VIEW_TYPE_ME
        else
            VIEW_TYPE_FRIENDS
    }

    override fun getItemCount(): Int {
        if (mFriendList == null)
            return 0
        return mFriendList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ME) {
            MyViewHolder(
                FriendMyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            UserViewHolder(FriendItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
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

    inner class MyViewHolder(private val binding: FriendMyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var userModel: UserModel? = null

        init {
            binding.setClickListener {
                groupMembers.clear()
                groupMembers[uid!!] = userModel
                itemClick.postValue(Event(true))
            }
        }

        fun bind(userModel: UserModel?) {
            userModel ?: return
            this.userModel = userModel

            binding.friendMyTextViewUsername.text = userModel.username

            if (userModel.profileImageUrl!!.isEmpty())
                binding.friendMyProfileImage.setImageResource(R.drawable.person)
            else
                Picasso.get().load(userModel.profileImageUrl).placeholder(R.drawable.person)
                    .into(binding.friendMyProfileImage)
        }
    }

    inner class UserViewHolder(private val binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var userModel: UserModel? = null

        init {
            binding.setClickListener {
                groupMembers.clear()
                groupMembers[uid!!] = mFriendList!![0]
                groupMembers[userModel!!.uid] = userModel
                itemClick.postValue(Event(true))

            }
        }

        fun bind(userModel: UserModel?) {
            if (userModel == null) return
            this.userModel = userModel
            binding.friendTextviewUsername.text = userModel.username
            if (userModel.profileImageUrl!!.isEmpty())
                binding.friendImageviewProfileImage.setImageResource(R.drawable.person)
            else
                Picasso.get().load(userModel.profileImageUrl).placeholder(R.drawable.person)
                    .into(binding.friendImageviewProfileImage)
        }
    }
}