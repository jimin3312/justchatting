package com.example.justchatting.ui.chattingRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.ChatMessageModel
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepository
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepositoryImpl
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChattingRoomViewModel : ViewModel(), KoinComponent{
    val chattingRoomRepository : ChattingRoomRepository by inject()

    fun fetchChatLog(groupId : String) {
        chattingRoomRepository.fetchChatLog(groupId)
    }
    fun getChatLogs() : LiveData<ArrayList<ChatMessageModel>>{
        return (chattingRoomRepository as ChattingRoomRepositoryImpl).chatLogsModel
    }

    fun setListener(groupId: String) {
        chattingRoomRepository.setListener(groupId)
    }
    fun loadGroupNameList(groupMembersMap: HashMap<String, Boolean>){
        chattingRoomRepository.loadGroupNameList(groupMembersMap)
    }

    fun makeRelationship(groupMembersMap: HashMap<String, Boolean>) {
        chattingRoomRepository.makeRelationship(groupMembersMap)
    }
    fun getNewGroupId(): LiveData<String>{
        return (chattingRoomRepository as ChattingRoomRepositoryImpl).newGroupId
    }
    fun sendText(text: String, groupId: String) {
        chattingRoomRepository.sendText(text, groupId)
    }

}