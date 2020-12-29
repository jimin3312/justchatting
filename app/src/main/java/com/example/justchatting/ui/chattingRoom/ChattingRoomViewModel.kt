package com.example.justchatting.ui.chattingRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.Message
import com.example.justchatting.UserModel
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChattingRoomViewModel : ViewModel(), KoinComponent{
    val chattingRoomRepository : ChattingRoomRepository by inject()
    var groupMembers : HashMap<String, UserModel>? = null

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
        chattingRoomRepository.pushFCM(text, groupMembers).observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({

            },{

            })
    }

    fun loadGroupMembers(groupId: String?) {
        chattingRoomRepository.loadGroupMembers(groupMembers, groupId)
    }

}