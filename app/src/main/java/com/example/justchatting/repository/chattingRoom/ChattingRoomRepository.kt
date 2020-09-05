package com.example.justchatting.repository.chattingRoom

interface ChattingRoomRepository {
    fun getChattingRooms()
    fun loadFriends()
    fun receiveMessage()
}