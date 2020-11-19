package com.example.justchatting

data class Message(
    val type : String = "",
    val id : String ="",
    val fromId: String = "",
    val profileImageUrl:String ="",
    val imageUri: String = "",
    val text : String = "",
    val timeStamp : Long = 0
)