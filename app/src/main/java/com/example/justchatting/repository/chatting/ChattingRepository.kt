package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.LatestMessage

interface ChattingRepository {
    fun setListener()
    fun getChattingRooms() : LiveData<ArrayList<LatestMessage>>
}