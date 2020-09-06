package com.example.justchatting.ui.chattingRoom

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.justchatting.R
import com.example.justchatting.User
import com.example.justchatting.databinding.ActivityChattingRoomBinding
import com.google.firebase.auth.FirebaseAuth

class ChattingRoomActivity : AppCompatActivity() {
    companion object{
        val TAG ="ChattingRoomsActivity"
    }

    private lateinit var groupMembersIdList : ArrayList<String>
    private lateinit var binding : ActivityChattingRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting_room)

        val myId = FirebaseAuth.getInstance().uid
        groupMembersIdList = intent.extras!!.getStringArrayList("groupMembersId")!!
        groupMembersIdList.add(myId)
        groupMembersIdList.sortBy { it }
        Log.d(TAG, "group member $groupMembersIdList")


    }

}