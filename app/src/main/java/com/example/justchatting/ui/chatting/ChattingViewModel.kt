package com.example.justchatting.ui.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.LatestMessage
import com.example.justchatting.repository.chatting.ChattingRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChattingViewModel : ViewModel(), KoinComponent{
    private val chattingRepository : ChattingRepository by inject()

    fun getChattingRooms(): LiveData<ArrayList<LatestMessage>> {
        return chattingRepository.getChattingRooms()
    }
    fun setListener() {
        chattingRepository.setListener()
    }
}