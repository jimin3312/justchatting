package com.example.justchatting

data class ChattingRoom(
    val lastMessage : String ="",
    val timeStamp : Long = 0,
    val groupId : String ="",
    var groupName : String =""
)