package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.data.DTO.ChattingRoom

interface ChattingRepository {
    fun chattingRoomListChangeListener()
    fun getChattingRooms() : LiveData<ArrayList<ChattingRoom>>
}