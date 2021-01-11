package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.data.DTO.ChattingRoom
import com.example.justchatting.data.chatting.ChattingFirebaseSource

class ChattingRepositoryImpl(private val chattingFirebaseSource: ChattingFirebaseSource) : ChattingRepository{

    override fun chattingRoomListChangeListener() {
        chattingFirebaseSource.chattingRoomListChangeListener()
    }
    override fun getChattingRooms(): LiveData<ArrayList<ChattingRoom>> {
        return chattingFirebaseSource.chattingRooms
    }

}