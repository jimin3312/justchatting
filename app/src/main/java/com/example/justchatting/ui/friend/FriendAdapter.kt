package com.example.justchatting.ui.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object{
        const val VIEW_TYPE_MY = 0
        const val VIEW_TYPE_USERS = 1
    }
    private var uid = FirebaseAuth.getInstance().uid
    private var mFriendList: ArrayList<UserModel>? = null
    var groupMembers = HashMap<String, Boolean>()

    var itemClick = MutableLiveData<Boolean>()

    fun setUsers(friendList: ArrayList<UserModel>) {
        mFriendList = friendList
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==0)
            VIEW_TYPE_MY
        else
            VIEW_TYPE_USERS
    }

    override fun getItemCount(): Int {
        if(mFriendList == null)
            return 0
        return mFriendList!!.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_MY) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_my_item, parent,false)
            MyViewHolder(view)
        } else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
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
        var userModel : UserModel? = null
        private val profileImg = itemView.findViewById<CircleImageView>(R.id.friend_my_imageview_profile_image)
        private val username=itemView.findViewById<TextView>(R.id.friend_my_textview_username)
        private val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.friend_my_item_constraint_layout)
        fun bind(userModel : UserModel?)
        {
            if(userModel == null) return
            this.userModel = userModel
            username.text = userModel.username
            if(userModel.profileImageUrl!!.isEmpty())
                profileImg.setImageResource(R.drawable.person)
            else
                Picasso.get().load(userModel.profileImageUrl).placeholder(R.drawable.person).into(profileImg)

            constraintLayout.setOnClickListener{
                groupMembers.clear()
                groupMembers[uid!!] = true
                itemClick.postValue(true)
            }
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var userModel : UserModel? = null
        private val profileImg = itemView.findViewById<CircleImageView>(R.id.friend_imageview_profile_image)
        private val username =itemView.findViewById<TextView>(R.id.friend_textview_username)
        private val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.friend_item_constraint_layout)
        fun bind(userModel : UserModel?)
        {
            if(userModel == null) return
            this.userModel = userModel
            username.text = userModel.username
            if(userModel.profileImageUrl!!.isEmpty())
                profileImg.setImageResource(R.drawable.person)
            else
                Picasso.get().load(userModel.profileImageUrl).placeholder(R.drawable.person).into(profileImg)

            constraintLayout.setOnClickListener{
                groupMembers.clear()
                groupMembers[uid!!] = true
                groupMembers[userModel.uid]= true
                itemClick.postValue(true)
            }
        }
    }


}