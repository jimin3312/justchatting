package com.example.justchatting

data class ChattingModel(
    val membersNameList : ArrayList<String> = ArrayList(),
    val membersIdMap : HashMap<String,Boolean> = HashMap(),
    val lastMessage : String ="",
    val timeStamp : Long = 0,
    val groupId : String =""
)