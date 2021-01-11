package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Event
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.data.chatting.SelectGroupFirebaseSource

class SelectGroupRepositoryImpl(
    private val selectGroupFirebaseSource: SelectGroupFirebaseSource
) : SelectGroupRepository {

    override fun loadFriends(alreadyEnteredMember: HashMap<String, UserModel>) {
        selectGroupFirebaseSource.loadFriends(alreadyEnteredMember)
    }
    override fun loadGroupId(groupMembers: HashMap<String, UserModel?>) {
        selectGroupFirebaseSource.loadGroupId(groupMembers)
    }

    override fun getFriends(): LiveData<ArrayList<UserModel>> {
        return selectGroupFirebaseSource.users
    }
    override fun getGroupId(): MutableLiveData<Event<String>> {
        return selectGroupFirebaseSource.groupId
    }
}