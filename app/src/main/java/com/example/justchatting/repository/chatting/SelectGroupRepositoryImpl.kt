package com.example.justchatting.repository.chatting

import androidx.lifecycle.LiveData
import com.example.justchatting.UserModel
import com.example.justchatting.data.chatting.SelectGroupFirebaseSource
import com.example.justchatting.data.friend.FriendFirebaseSource

class SelectGroupRepositoryImpl(
    private val friendFirebaseSource: FriendFirebaseSource,
    private val selectGroupFirebaseSource: SelectGroupFirebaseSource
) : SelectGroupRepository {

    override fun loadFriends() {
        friendFirebaseSource.loadFriends()
    }
    override fun loadGroupId(groupMembers: HashMap<String, Boolean>) {
        selectGroupFirebaseSource.loadGroupId(groupMembers)
    }

    override fun getFriends(): LiveData<ArrayList<UserModel>> {
        return friendFirebaseSource.users
    }
    override fun getGroupId(): LiveData<String> {
        return selectGroupFirebaseSource.groupId
    }
}