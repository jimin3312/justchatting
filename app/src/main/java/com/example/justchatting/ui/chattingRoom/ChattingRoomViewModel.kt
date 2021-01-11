package com.example.justchatting.ui.chattingRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.JustApp
import com.example.justchatting.data.DTO.Message
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepository
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChattingRoomViewModel : ViewModel(), KoinComponent{
    val chattingRoomRepository : ChattingRoomRepository by inject()
    var groupMembers : HashMap<String, UserModel> = HashMap()
    var groupId : String = ""

    fun getChatLogs() : LiveData<ArrayList<Message>>{
        return chattingRoomRepository.getChatLogs()
    }
    fun getNewGroupId(): LiveData<String>{
        return chattingRoomRepository.getNewGroupId()
    }
    fun setListener(groupId: String) {
        chattingRoomRepository.setListener(groupId)
    }
    fun createGroupId() {
        chattingRoomRepository.createGroupId(groupMembers)
    }
    fun sendText(text: String, groupId: String) {
        chattingRoomRepository.sendText(text, groupId)
        chattingRoomRepository.pushFCM(text, groupMembers, groupId).observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation())
            .subscribe({

            },{

            })
    }

    fun loadGroupMembers(groupId: String?) {
        chattingRoomRepository.loadGroupMembers(groupMembers, groupId)
    }

    fun getMembers(): LiveData<ArrayList<UserModel>> {
        return chattingRoomRepository.getMembers()
    }

    override fun onCleared() {
        JustApp.roomId = ""
        super.onCleared()
    }

    fun addMember() {
        chattingRoomRepository.addMember(groupMembers, groupId)
    }

    fun exit() {
        chattingRoomRepository.exit(groupId)
    }

}