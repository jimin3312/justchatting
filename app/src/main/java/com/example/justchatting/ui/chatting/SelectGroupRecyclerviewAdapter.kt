package com.example.justchatting.ui.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.databinding.SelectGroupItemBinding
import com.squareup.picasso.Picasso

class SelectGroupRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mFriendList: ArrayList<UserModel>? = null

    private var _checkedCnt = MutableLiveData<Int>()
    val checkedCnt: LiveData<Int>
        get() = _checkedCnt

    var groupMembers = HashMap<String, UserModel?>()

    fun setFriendList(friendList: ArrayList<UserModel>) {
        mFriendList = friendList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FriendViewHolder(
            SelectGroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        if (mFriendList == null)
            return 0
        return mFriendList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FriendViewHolder).bind(mFriendList!![position])
    }

    inner class FriendViewHolder(binding: SelectGroupItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var userModel: UserModel? = null
        val username = binding.selectTextviewUsername
        val profileImage = binding.selectImageviewProfileImage
        val checkBox = binding.selectGroupCheckbox

        init {
            binding.setOnClickListener {
                if (checkBox.isChecked) {
                    checkBox.isChecked = false
                    groupMembers.remove(userModel!!.uid)
                } else {
                    checkBox.isChecked = true
                    groupMembers[userModel!!.uid] = userModel
                }
                _checkedCnt.postValue(groupMembers.size)
            }
        }

        fun bind(userModel: UserModel?) {
            if (userModel == null)
                return

            this.userModel = userModel
            username.text = userModel.username
            if (userModel.profileImageUrl!!.isEmpty()) {
                profileImage.setImageResource(R.drawable.person)
            } else {
                Picasso.get().load(userModel.profileImageUrl).placeholder(R.drawable.person)
                    .into(profileImage)
            }
        }
    }
}