package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.LatestMessage
import com.example.justchatting.data.chatting.ChattingFirebaseSource

class ChattingRepositoryImpl(private val chattingFirebaseSource: ChattingFirebaseSource) : ChattingRepository{

    override fun setListener() {
        chattingFirebaseSource.setListener()
    }
    override fun getChattingRooms(): LiveData<ArrayList<LatestMessage>> {
        return chattingFirebaseSource.chattingRooms
    }

}