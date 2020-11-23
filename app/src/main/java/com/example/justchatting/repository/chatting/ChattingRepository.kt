package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.ChattingRoom

interface ChattingRepository {
    fun setListener()
    fun getChattingRooms() : LiveData<ArrayList<ChattingRoom>>
}