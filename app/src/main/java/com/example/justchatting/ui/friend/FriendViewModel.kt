package com.example.justchatting.ui.friend

import android.app.Application
import androidx.lifecycle.*
import com.example.justchatting.UserModel
import com.example.justchatting.repository.chatting.SelectGroupRepository
import com.example.justchatting.repository.friend.FriendRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val selectGroupRepository : SelectGroupRepository by inject()
    private val friendRepository : FriendRepository by inject()

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
        friendRepository.makeFriendRelationships(getApplication())
    }
    fun addFriendWithPhoneNumber(input: String) {
        friendRepository.addFriendWithPhoneNumber(input)
    }
    fun addFriendWithId(input: String) {
        friendRepository.addFriendWithEmail(input)
    }
    fun getGroupId() : MutableLiveData<String>{
        return  selectGroupRepository.getGroupId()
    }
    fun loadGroupId(groupMembers: HashMap<String, UserModel>) {
        selectGroupRepository.loadGroupId(groupMembers)
    }
}