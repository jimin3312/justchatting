package com.example.justchatting.repository.chattingRoom

import androidx.lifecycle.LiveData
import com.example.justchatting.Message
import com.example.justchatting.UserModel
import io.reactivex.Completable

interface ChattingRoomRepository {

    fun getNewGroupId(): LiveData<String>
    fun getChatLogs(): LiveData<ArrayList<Message>>
    fun getMembers() : LiveData<ArrayList<UserModel>>
    fun sendText(
        text: String,
        groupId: String
    )
    fun setListener(groupId: String)
    fun createGroupId(groupMembersMap: HashMap<String, UserModel>)
    fun loadGroupMembers(
        groupMembers: java.util.HashMap<String, UserModel>,
        groupId: String?
    )

    fun pushFCM(
        text: String,
        groupMembers: HashMap<String, UserModel>,
        groupId: String
    ) : Completable

    fun addMember(
        member: java.util.HashMap<String, UserModel>,
        groupId: String
    )
    fun exit(groupId: String)
}