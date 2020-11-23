package com.example.justchatting.ui.chattingRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.Message
import com.example.justchatting.UserModel
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChattingRoomViewModel : ViewModel(), KoinComponent{
    val chattingRoomRepository : ChattingRoomRepository by inject()

//    fun fetchChatLog(groupId : String) {
//        chattingRoomRepository.fetchChatLog(groupId)
//    }
    fun getChatLogs() : LiveData<ArrayList<Message>>{
        return chattingRoomRepository.getChatLogs()
    }
    fun getNewGroupId(): LiveData<String>{
        return chattingRoomRepository.getNewGroupId()
    }
    fun setListener(groupId: String) {
        chattingRoomRepository.setListener(groupId)
    }
    fun loadGroupNameList(groupMembersMap: HashMap<String, UserModel>){
//        chattingRoomRepository.loadGroupNameList(groupMembersMap)
    }

    fun createGroupId(groupMembersMap: HashMap<String, UserModel>) {
        chattingRoomRepository.createGroupId(groupMembersMap)
    }
    fun sendText(text: String, groupId: String) {
        chattingRoomRepository.sendText(text, groupId)
    }

}