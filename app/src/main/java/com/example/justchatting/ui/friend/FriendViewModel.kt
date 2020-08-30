package com.example.justchatting.ui.friend

import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.*
import com.example.justchatting.User

import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val friendRepository : FriendRepository by inject()

    init {
        friendRepository.setListener()
    }
    fun getMyUsers() : MutableLiveData<User> {
        return friendRepository.myUser
    }
    fun getUsers() : MutableLiveData<ArrayList<User>> {
        return friendRepository.friendUsers
    }

    fun sync() {
        friendRepository.makeFriendRelationships(getApplication())
        friendRepository.loadMyUser()
        friendRepository.loadFriends()
    }
}