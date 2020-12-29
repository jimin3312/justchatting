package com.example.justchatting.repository.chattingRoom

import androidx.lifecycle.LiveData
import com.example.justchatting.Message
import com.example.justchatting.UserModel
import com.example.justchatting.data.chattingRoom.ChattingRoomFirebaseSource
import io.reactivex.Completable
import kotlin.collections.ArrayList

class ChattingRoomRepositoryImpl(private val chattingRoomFirebaseSource: ChattingRoomFirebaseSource) :
    ChattingRoomRepository {

    companion object {
        val TAG = "ChattingRoomRepo"
    }
    override fun getChatLogs(): LiveData<ArrayList<Message>> {
        return chattingRoomFirebaseSource.logs
    }
    override fun getNewGroupId(): LiveData<String> {
        return chattingRoomFirebaseSource.newGroupId
    }
//    override fun loadGroupNameList(groupMembersMap: HashMap<String, UserModel>) {
//        chattingRoomFirebaseSource.loadGroupNameList(groupMembersMap)
//    }

//    override fun fetchChatLog(groupId: String) {
//        chattingRoomFirebaseSource.fetchChatLog(groupId)
//    }

    override fun setListener(groupId: String) {
        chattingRoomFirebaseSource.setListener(groupId)
    }

    override fun createGroupId(groupMembersMap: HashMap<String, UserModel>?) {
        chattingRoomFirebaseSource.createGroupId(groupMembersMap)
    }

    override fun loadGroupMembers(
        groupMembers: java.util.HashMap<String, UserModel>?,
        groupId: String?
    ) {
        chattingRoomFirebaseSource.loadGroupMembers(groupMembers, groupId)
    }

    override fun pushFCM(
        text : String,
        groupMembers: HashMap<String, UserModel>?
    ): Completable = chattingRoomFirebaseSource.pushFCM(text, groupMembers)



    override fun sendText(
        text: String,
        groupId: String
    ) {
        chattingRoomFirebaseSource.sendText(text, groupId)
    }


}