package com.example.justchatting.repository.chattingRoom

interface ChattingRoomRepository {
    fun getChattingRooms()
    fun makeNewChattingRoom()
    fun receiveMessage()
}