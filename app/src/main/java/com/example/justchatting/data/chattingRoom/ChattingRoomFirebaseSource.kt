package com.example.justchatting.data.chattingRoom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.*
import com.example.justchatting.data.remote.NotificationAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.StringBuilder

class ChattingRoomFirebaseSource : KoinComponent{

    private val notificationAPI : NotificationAPI by inject()
    private var logArrayList: ArrayList<Message> = ArrayList()

    private var _chatLogs = MutableLiveData<ArrayList<Message>>()
    val logs: LiveData<ArrayList<Message>>
        get() = _chatLogs

    private var _newGroupId = MutableLiveData<String>()
    val newGroupId: LiveData<String>
        get() = _newGroupId

    companion object {
        val TAG = "ChattingRoomRepo"
    }

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

        val uid = FirebaseAuth.getInstance().uid
        val groupId = FirebaseDatabase.getInstance().getReference("/chatrooms").push().key
        val membersRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
        membersRef.setValue(groupMembersMap).addOnCompleteListener {
            groupMembersMap.forEach {
                val userGroupRef = FirebaseDatabase.getInstance().getReference("/user_groups/${it.key}/$groupId")
                userGroupRef.setValue(createGroupName(groupMembersMap, it))
            }
            _newGroupId.postValue(groupId)
        }

        if(groupMembersMap.size==2){
            var friendId = ""
            groupMembersMap.forEach{
                if(it.key != uid){
                    friendId = it.key
                }
            }
            val fromRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
            fromRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Friend::class.java)?: return
                    val changeData = Friend(data.isNotBlocked, groupId!!)
                    fromRef.setValue(changeData)
                }
            })

            val toRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
            toRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Friend::class.java)?: return
                    val changeData = Friend(data.isNotBlocked, groupId!!)
                    toRef.setValue(changeData)
                }
            })

        }

    }

    private fun createGroupName(
        groupMembersMap: HashMap<String, UserModel>,
        member : Map.Entry<String, UserModel>
    ): String {
        val stringBuilder = StringBuilder()

        for( entry in groupMembersMap.entries){
            if(member != entry || groupMembersMap.size == 1)
                stringBuilder.append(entry.value.username+',')

            if(stringBuilder.length>=19)
                break
        }

        stringBuilder.deleteCharAt(stringBuilder.length-1)

        return stringBuilder.toString()
    }

    fun sendText(
        text: String,
        groupId: String
    ) {
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

    fun loadGroupMembers(
        groupMembers: HashMap<String, UserModel>,
        groupId: String?
    ) {
        val membersRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
        membersRef.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val user = it.getValue(UserModel::class.java)?: return /* handle exception*/
                    groupMembers[user.uid] = user;
                }
            }
        })
    }

    fun pushFCM(
        text : String,
        groupMembers: HashMap<String, UserModel>
    ) : Completable {
        var notificationInfo = NotificationInfo(groupMembers[FirebaseAuth.getInstance().uid]!!.username, text)
        var registrationIds  =  TokenParser()
            .parser(groupMembers)

        var notificationRequest : NotificationRequest = NotificationRequest(registrationIds, notificationInfo)

        Log.d("테스트",notificationRequest.toString())
        return notificationAPI.pushMessage(notificationRequest)
    }
}