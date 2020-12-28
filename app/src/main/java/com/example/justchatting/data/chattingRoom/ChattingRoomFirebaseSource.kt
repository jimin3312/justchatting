package com.example.justchatting.data.chattingRoom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Message
import com.example.justchatting.ChattingRoom
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.StringBuilder

class ChattingRoomFirebaseSource {

    private var logArrayList: ArrayList<Message> = ArrayList()

    private var groupNameList: ArrayList<String> = ArrayList()

    private var _chatLogs = MutableLiveData<ArrayList<Message>>()
    val logs: LiveData<ArrayList<Message>>
        get() = _chatLogs

    private var _newGroupId = MutableLiveData<String>()
    val newGroupId: LiveData<String>
        get() = _newGroupId

    companion object {
        val TAG = "ChattingRoomRepo"
    }
//
//    fun loadGroupNameList(groupMembersMap: HashMap<String, UserModel>) {
//
//        val groupMembers: ArrayList<String> = ArrayList(groupMembersMap.keys)
//        groupMembers.sortBy { it }
//
//        groupMembers!!.forEach {
//            Log.d(TAG, "key : ${it}")
//            val userRef = FirebaseDatabase.getInstance().getReference("/users/${it}")
//            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val user = snapshot.getValue(UserModel::class.java) ?: return
//                    Log.d(TAG, "username :${user.username}")
//                    groupNameList.add(user.username)
//                }
//            })
//        }
//    }

//    fun fetchChatLog(groupId: String) {
//        chatLogArrayList = ArrayList()
//        val chatLogRef = FirebaseDatabase.getInstance().getReference("/messages/$groupId")
//            .orderByChild("timestamp")
//        chatLogRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach { dataSnapshot ->
//                    val chatMessage = dataSnapshot.getValue(ChatMessageModel::class.java) ?: return
//                    chatLogArrayList.add(chatMessage)
//                    _chatLogs.postValue(chatLogArrayList)
//                }
//            }
//        })
//    }

    fun setListener(groupId: String) {
        val chatLogRef = FirebaseDatabase.getInstance().getReference("/messages/$groupId")
        chatLogRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "Listener onChildAdd")
                val chatMessage = snapshot.getValue(Message::class.java) ?: return
                Log.d(TAG, "chat : ${chatMessage.text}")

                logArrayList.add(chatMessage)
                _chatLogs.postValue(logArrayList)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    fun createGroupId(groupMembersMap: HashMap<String, UserModel>) {
        val groupId = FirebaseDatabase.getInstance().getReference("/chatrooms").push().key
        val membersRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
        membersRef.setValue(groupMembersMap).addOnCompleteListener {
            groupMembersMap.forEach {
                val userGroupRef = FirebaseDatabase.getInstance().getReference("/user_groups/${it.key}/$groupId")
                userGroupRef.setValue(createGroupName(groupMembersMap, it))
            }
            _newGroupId.postValue(groupId)
        }
    }

    private fun createGroupName(
        groupMembersMap: HashMap<String, UserModel>,
        member : Map.Entry<String, UserModel>
    ): String {
        val stringBuilder = StringBuilder()

        for( entry in groupMembersMap.entries){
            if(member != entry)
                stringBuilder.append(entry.value.username+',')

            if(stringBuilder.length>=19)
                break
        }
        stringBuilder.deleteCharAt(stringBuilder.length-1)

        return stringBuilder.toString()
    }

    fun sendText(text: String, groupId: String) {
        val uid = FirebaseAuth.getInstance().uid
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java) ?: return
                var messageRef: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("/messages/$groupId").push()
                val chatMessage = Message(
                    "text",
                    messageRef.key.toString(),
                    uid!!,
                    user.profileImageUrl!!,
                    "",
                    text,
                    System.currentTimeMillis(),
                    user.username
                )
                messageRef.setValue(chatMessage)

                var chattingRoom: ChattingRoom
                chattingRoom = if (chatMessage.type == "text")
                    ChattingRoom(
                        chatMessage.text,
                        chatMessage.timeStamp,
                        groupId
                    )
                else
                    ChattingRoom(
                        "사진",
                        chatMessage.timeStamp,
                        groupId
                    )

                val chatRoomRef =
                    FirebaseDatabase.getInstance().getReference("/chatrooms/$groupId")
                chatRoomRef.setValue(chattingRoom)
            }
        })
    }

}