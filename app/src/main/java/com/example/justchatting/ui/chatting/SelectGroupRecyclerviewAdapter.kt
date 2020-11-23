package com.example.justchatting.ui.chatting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.UserModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SelectGroupRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mFriendList : ArrayList<UserModel>? = null

    private var _checkedCnt = MutableLiveData<Int>()
    val checkedCnt : LiveData<Int>
        get()=_checkedCnt

    var groupMembers = HashMap<String, UserModel>()

    fun setFriendList(friendList : ArrayList<UserModel>){
        mFriendList = friendList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_group_item,parent,false)
        return FriendViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(mFriendList == null)
            return 0
        return mFriendList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FriendViewHolder).bind(mFriendList!![position])
    }
    inner class FriendViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var userModel : UserModel? = null
        val username = itemView.findViewById<TextView>(R.id.select_textview_username)
        val profileImage =  itemView.findViewById<CircleImageView>(R.id.select_imageview_profile_image)
        val checkBox = itemView.findViewById<CheckBox>(R.id.select_group_checkbox)
        val layout: ConstraintLayout = itemView.findViewById(R.id.select_fried_item)

        fun bind(userModel : UserModel?){
            if(userModel == null)
                return

            this.userModel=userModel
            username.text = userModel.username
            if(userModel.profileImageUrl!!.isEmpty()){
                profileImage.setImageResource(R.drawable.person)
            } else{
                Picasso.get().load(userModel.profileImageUrl).placeholder(R.drawable.person).into(profileImage)
            }

            layout.setOnClickListener{
                if(checkBox.isChecked) {
                    checkBox.isChecked = false
                    groupMembers.remove(userModel.uid)
                } else {
                    checkBox.isChecked = true
                    groupMembers[userModel.uid] = userModel
                }
                _checkedCnt.postValue(groupMembers.size)
            }
        }
    }
}