package com.example.justchatting

class ChatMessageText(
    override val type : Int =0,
    val id : String ="",
    val fromId: String = "",
    val profileImageUrl:String ="",
    val text: String = "",
    val timeStamp : Long = 0
)   : ChatMessage(type)