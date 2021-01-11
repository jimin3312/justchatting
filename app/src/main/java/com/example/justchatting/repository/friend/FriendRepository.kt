package com.example.justchatting.repository.friend

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Event
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.ui.friend.FriendFragment

interface FriendRepository {
    fun getUsers(): LiveData<ArrayList<UserModel>>

    fun makeFriendRelationships(application: Application)

    fun addFriendWithEmail(email : String, friends: HashMap<String, UserModel>)

    fun addFriendWithPhoneNumber(phoneNum : String, friends: HashMap<String, UserModel>)

    fun setFriendListChangeListener(friends: HashMap<String, UserModel>)

    fun isValidToAdd(): MutableLiveData<Event<FriendFragment.AddResult>>
}
