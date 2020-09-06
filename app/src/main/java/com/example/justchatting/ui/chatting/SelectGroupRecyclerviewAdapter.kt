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
import com.example.justchatting.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SelectGroupRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mFriendList : ArrayList<User>? = null

    private var _checkedCnt = MutableLiveData<Int>()
    val checkedCnt : LiveData<Int>
        get()=_checkedCnt
    var checkedArrayList = ArrayList<String>()
    fun setFriendList(friendList : ArrayList<User>){
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
        var user : User? = null
        val username = itemView.findViewById<TextView>(R.id.select_textview_username)
        val profileImage =  itemView.findViewById<CircleImageView>(R.id.select_imageview_profile_image)
        val checkBox = itemView.findViewById<CheckBox>(R.id.select_group_checkbox)
        val layout: ConstraintLayout = itemView.findViewById(R.id.select_fried_item)

        fun bind(user : User?){
            if(user == null)
                return

            this.user=user
            username.text = user.username
            if(user.profileImageUrl!!.isEmpty()){
                profileImage.setImageResource(R.drawable.person)
            } else{
                Picasso.get().load(user.profileImageUrl).placeholder(R.drawable.person).into(profileImage)
            }

            layout.setOnClickListener{
                if(checkBox.isChecked) {
                    checkBox.isChecked = false
                    val it = checkedArrayList.iterator()
                    while(it.hasNext()){
                        if(it.next() == user.uid){
                            it.remove()
                        }
                    }
                } else {
                    checkBox.isChecked = true
                    checkedArrayList.add(user.uid)
                }
                _checkedCnt.postValue(checkedArrayList.size)
            }
        }
    }
}