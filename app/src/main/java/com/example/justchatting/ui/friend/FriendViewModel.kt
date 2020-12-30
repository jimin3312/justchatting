package com.example.justchatting.ui.friend

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.justchatting.UserModel
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import com.example.justchatting.repository.friend.FriendChattingRepository
import com.example.justchatting.repository.friend.FriendRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val friendRepository : FriendRepository by inject()
    private val friendChattingRepository : FriendChattingRepository by inject()

    fun setListener(){
        friendRepository.setListener()
    }
    fun getUsers() : LiveData<ArrayList<UserModel>> {
        return friendRepository.getUsers()
    }

    fun getAddFriend() : MutableLiveData<Int>{
        return friendRepository.getAddFriend()
    }
    fun sync() {
        if(friendRepository.getUsers().value!!.isEmpty()) {
            friendRepository.makeFriendRelationships(getApplication())
        }
    }
    fun addFriendWithPhoneNumber(input: String) {
        friendRepository.addFriendWithPhoneNumber(input)
    }
    fun addFriendWithId(input: String) {
        friendRepository.addFriendWithEmail(input)
    }
    fun getGroupId() : MutableLiveData<String>{
        return  friendChattingRepository.groupId
    }
    fun loadGroupId(groupMembers: HashMap<String, UserModel>) {
        friendChattingRepository.loadGroupId(groupMembers)
    }

    fun loadMyInfo() {
        friendRepository.loadMyInfo()
    }
}