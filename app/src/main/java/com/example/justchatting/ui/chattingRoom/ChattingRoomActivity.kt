package com.example.justchatting.ui.chattingRoom

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.R
import com.example.justchatting.databinding.ActivityChattingRoomBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chatting_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChattingRoomActivity : AppCompatActivity() {
    companion object{
        val TAG ="ChattingRoomsActivity"
    }
    private val viewModel : ChattingRoomViewModel by viewModel()
    private lateinit var groupMembersIdList : ArrayList<String>
    private lateinit var chattingRoomAdapter: ChattingRoomAdapter
    private lateinit var binding : ActivityChattingRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting_room)

        val myId = FirebaseAuth.getInstance().uid
        groupMembersIdList = intent.extras!!.getStringArrayList("groupMembersId")!!
        groupMembersIdList.add(myId)
        groupMembersIdList.sortBy { it }
        Log.d(TAG, "group member $groupMembersIdList")

        chattingRoomAdapter = ChattingRoomAdapter()

        chatting_room_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chattingRoomAdapter
        }


    }

}