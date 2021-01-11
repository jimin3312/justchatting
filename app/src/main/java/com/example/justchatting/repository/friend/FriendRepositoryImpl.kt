package com.example.justchatting.repository.friend

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Event
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.data.friend.ContactsDAO
import com.example.justchatting.data.friend.FriendAdditionFirebaseSource
import com.example.justchatting.data.friend.FriendFirebaseSource
import com.example.justchatting.ui.friend.FriendFragment.AddResult

class FriendRepositoryImpl(
    private val contactsDAO: ContactsDAO,
    private val friendFirebaseSource: FriendFirebaseSource,
    private val friendAdditionFirebaseSource: FriendAdditionFirebaseSource
) : FriendRepository {

    override fun getUsers(): LiveData<ArrayList<UserModel>> {
        return friendFirebaseSource.users
    }

    override fun makeFriendRelationships(application: Application) =
        contactsDAO.makeFriendRelationships(application)

    override fun addFriendWithEmail(email: String, friends: HashMap<String, UserModel>) {
        friendAdditionFirebaseSource.addFriendWithEmail(email, friends)
    }

    override fun addFriendWithPhoneNumber(phoneNum: String, friends: HashMap<String, UserModel>) {
        friendAdditionFirebaseSource.addFriendWithPhoneNumber(phoneNum, friends)
    }

    override fun setFriendListChangeListener(friends: HashMap<String, UserModel>) {
        friendFirebaseSource.setFriendListChangeListener(friends)
    }

    override fun isValidToAdd(): MutableLiveData<Event<AddResult>> {
        return friendAdditionFirebaseSource.isValidToAdd
    }
}