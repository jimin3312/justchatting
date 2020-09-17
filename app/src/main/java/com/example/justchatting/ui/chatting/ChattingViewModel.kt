package com.example.justchatting.ui.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.justchatting.ChattingModel
import com.example.justchatting.repository.chatting.ChattingRepository
import com.example.justchatting.repository.chatting.ChattingRepositoryImpl
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChattingViewModel : ViewModel(), KoinComponent{
    private val chattingRepository : ChattingRepository by inject()
    init {
        chattingRepository.setListener()
    }
    fun getChattingRooms(): LiveData<ArrayList<ChattingModel>> {
        return (chattingRepository as ChattingRepositoryImpl).chattingRooms
    }
    fun loadChattingRooms() {
        chattingRepository.loadChatRooms()
    }
}