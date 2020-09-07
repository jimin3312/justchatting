package com.example.justchatting.ui.chattingRoom

import androidx.lifecycle.ViewModel
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ChattingRoomViewModel : ViewModel(), KoinComponent{
    val chattingRoomRepository : ChattingRoomRepository by inject()


}