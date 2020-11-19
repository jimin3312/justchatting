package com.example.justchatting.repository.chattingRoom

import androidx.lifecycle.LiveData
import com.example.justchatting.Message
import com.example.justchatting.UserModel

interface ChattingRoomRepository {

    fun getNewGroupId(): LiveData<String>
    fun getChatLogs(): LiveData<ArrayList<Message>>
    fun sendText(text: String, groupId: String)
    fun setListener(groupId: String)
    fun createGroupId(groupMembersMap: HashMap<String, UserModel>)
    fun loadGroupNameList(groupMembersMap: HashMap<String, UserModel>)
//    fun fetchChatLog(groupId : String)
}