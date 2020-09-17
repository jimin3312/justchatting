package com.example.justchatting.repository.chattingRoom

interface ChattingRoomRepository {
    fun fetchChatLog(groupId : String)
    fun sendText(text: String, groupId: String)
    fun setListener(groupId: String)
    fun makeRelationship(groupMembersMap: HashMap<String, Boolean>)
    fun loadGroupNameList(groupMembersMap: HashMap<String, Boolean>)
}