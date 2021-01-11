package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Event
import com.example.justchatting.data.DTO.UserModel

interface SelectGroupRepository {
    fun loadFriends(alreadyEnteredMember: HashMap<String, UserModel>)
    fun loadGroupId(groupMembers: HashMap<String, UserModel?>)
    fun getFriends(): LiveData<ArrayList<UserModel>>
    fun getGroupId(): MutableLiveData<Event<String>>
}