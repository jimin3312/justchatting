package com.example.justchatting.repository.friend

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.UserModel
import com.example.justchatting.data.friend.ContactsDAO
import com.example.justchatting.data.friend.FriendFirebaseSource

class FriendRepository(private val contactsDAO: ContactsDAO, private val friendFirebaseSource: FriendFirebaseSource) {

    fun getUsers(): LiveData<ArrayList<UserModel>> {
        return friendFirebaseSource.users
    }
    fun makeFriendRelationships(application: Application) = contactsDAO.makeFriendRelationships(application)

    fun addFriendWithEmail(email : String){
        friendFirebaseSource.addFriendWithEmail(email)
    }
    fun addFriendWithPhoneNumber(phoneNum : String){
        friendFirebaseSource.addFriendWithPhoneNumber(phoneNum)
    }
    fun setListener() {
        friendFirebaseSource.setListener()
    }
    fun getAddFriend(): MutableLiveData<Int> {
        return friendFirebaseSource.addFriend
    }
}