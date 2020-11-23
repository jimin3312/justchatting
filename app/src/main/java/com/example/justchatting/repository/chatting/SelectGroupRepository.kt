package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.UserModel

interface SelectGroupRepository {
    fun loadFriends()
    fun loadGroupId(groupMembers: HashMap<String,UserModel>)
    fun getFriends(): LiveData<ArrayList<UserModel>>
    fun getGroupId(): LiveData<String>
}