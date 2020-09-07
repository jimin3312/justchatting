package com.example.justchatting

class ChatMessageImage (
    override val type : Int =0,
    val id : String ="",
    val fromId: String = "",
    val profileImageUrl:String ="",
    val imageUri: String = "",
    val timeStamp : Long = 0
)   : ChatMessage(type)