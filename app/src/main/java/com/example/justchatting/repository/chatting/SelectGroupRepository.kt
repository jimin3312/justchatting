package com.example.justchatting.repository.chatting

interface SelectGroupRepository {
    fun loadFriends()
    fun loadGroupId(groupMembers: HashMap<String,Boolean>)
}