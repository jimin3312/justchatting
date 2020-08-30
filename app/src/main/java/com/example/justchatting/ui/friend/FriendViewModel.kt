package com.example.justchatting.ui.friend

import android.app.Application
import androidx.lifecycle.*
import com.example.justchatting.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val friendRepository : FriendRepository by inject()
    init {
        friendRepository.setListener()
    }
    fun getMyUsers() : LiveData<User> {
        return friendRepository.myUser
    }
    fun getUsers() : LiveData<ArrayList<User>> {
        return friendRepository.friendUsers
    }
    fun getAddFriend() : LiveData<Boolean>{
        return friendRepository.addFriend
    }
    fun sync() {
        friendRepository.makeFriendRelationships(getApplication())
        friendRepository.loadMyUser()
        friendRepository.loadFriends()
    }
    fun addFriendWithPhoneNumber(input: String) {
        friendRepository.addFriendWithPhoneNumber(input)
    }
    fun addFriendWithId(input: String) {
        friendRepository.addFriendWithId(input)
    }

}