package com.example.justchatting.ui.chattingRoom

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
    private var groupId : String? = null
    private lateinit var groupMembersMap : HashMap<String, Boolean>
    private lateinit var chattingRoomAdapter: ChattingRoomAdapter
    private lateinit var binding : ActivityChattingRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting_room)

        val myId = FirebaseAuth.getInstance().uid

        groupId = intent.getStringExtra("groupId")
        groupMembersMap = intent.getSerializableExtra("groupMembersMap") as HashMap<String, Boolean>
        viewModel.loadGroupNameList(groupMembersMap)

        Log.d(TAG,"groupID : ${groupId}")
        Log.d(TAG, "groupMembers : $groupMembersMap")
        chattingRoomAdapter = ChattingRoomAdapter()

        chatting_room_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chattingRoomAdapter
        }

        if(groupId != ""){
            Log.d(TAG, "fetch $groupId")
            viewModel.setListener(groupId!!)
//            viewModel.fetchChatLog(groupId!!)
        }

        viewModel.getChatLogs().observe(this, Observer {
            chattingRoomAdapter.setChattingLog(it)
            chattingRoomAdapter.notifyDataSetChanged()
            chatting_room_recyclerview.scrollToPosition(it.size-1)
        })
        viewModel.getNewGroupId().observe(this, Observer {newId->
            Log.d(TAG, "new group id : $newId")
            groupId = newId
            viewModel.setListener(groupId!!)
            viewModel.sendText(chatting_room_edittext_input.text.toString(), groupId!!)
            chatting_room_edittext_input.setText("")
        })


        chatting_room_button_send.setOnClickListener {
            if(chatting_room_edittext_input.text.isNotEmpty()){
                if(groupId==""){
                    viewModel.makeRelationship(groupMembersMap)
                } else{
                    viewModel.sendText(chatting_room_edittext_input.text.toString(), groupId!!)
                    chatting_room_edittext_input.setText("")
                }
            }
        }
    }

}