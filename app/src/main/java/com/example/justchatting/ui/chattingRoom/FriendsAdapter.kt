package com.example.justchatting.ui.chattingRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserViewHolder).bind(mFriendList!![position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var userModel : UserModel? = null
        private val profileImg = itemView.findViewById<CircleImageView>(R.id.friend_imageview_profile_image)
        private val username =itemView.findViewById<TextView>(R.id.friend_textview_username)
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
