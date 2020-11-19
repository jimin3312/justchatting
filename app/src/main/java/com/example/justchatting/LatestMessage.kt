package com.example.justchatting

data class LatestMessage(
    val lastMessage : String ="",
    val timeStamp : Long = 0,
    val groupId : String =""
)