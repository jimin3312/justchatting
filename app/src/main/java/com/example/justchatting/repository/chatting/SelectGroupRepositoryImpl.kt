package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.UserModel
import com.example.justchatting.data.chatting.SelectGroupFirebaseSource
import com.example.justchatting.data.friend.FriendFirebaseSource

class SelectGroupRepositoryImpl(
    private val selectGroupFirebaseSource: SelectGroupFirebaseSource
) : SelectGroupRepository {

    override fun loadFriends() {
        selectGroupFirebaseSource.loadFriends()
    }
    override fun loadGroupId(groupMembers: HashMap<String, UserModel>) {
        selectGroupFirebaseSource.loadGroupId(groupMembers)
    }

    override fun getFriends(): LiveData<ArrayList<UserModel>> {
        return selectGroupFirebaseSource.users
    }
    override fun getGroupId(): LiveData<String> {
        return selectGroupFirebaseSource.groupId
    }
}