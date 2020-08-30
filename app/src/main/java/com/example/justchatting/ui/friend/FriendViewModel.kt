package com.example.justchatting.ui.friend

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.justchatting.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val friendRepository : FriendRepository by inject()
    init {
        Log.d("FriedViewModel", "init")
        friendRepository.setListener()
    }
    fun getUsers() : LiveData<ArrayList<User>> {
        return friendRepository.users
    }
    fun getAddFriend() : MutableLiveData<Int>{
        return friendRepository.addFriend
    }
    fun sync() {
        Log.d("FriedViewModel","sync ${friendRepository.friendMap.size}")
        if(friendRepository.friendMap.size==0) {
            friendRepository.makeFriendRelationships(getApplication())
            friendRepository.loadFriends()
        }
    }
    fun addFriendWithPhoneNumber(input: String) {
        Log.d("FriedViewModel", input)
        friendRepository.addFriendWithPhoneNumber(input)
    }
    fun addFriendWithId(input: String) {
        Log.d("FriedViewModel", input)
        friendRepository.addFriendWithEmail(input)
    }

}