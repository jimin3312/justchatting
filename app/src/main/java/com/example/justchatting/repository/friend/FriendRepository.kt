package com.example.justchatting.repository.friend

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.UserModel

interface FriendRepository {
    fun getUsers(): LiveData<ArrayList<UserModel>>

    fun makeFriendRelationships(application: Application)

    fun addFriendWithEmail(email : String, friends: HashMap<String, UserModel>)

    fun addFriendWithPhoneNumber(phoneNum : String, friends: HashMap<String, UserModel>)

    fun setFriendListChangeListener(friends: HashMap<String, UserModel>)

    fun isValidToAdd(): MutableLiveData<Int>
}
