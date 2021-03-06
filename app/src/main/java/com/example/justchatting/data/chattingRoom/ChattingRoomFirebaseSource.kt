package com.example.justchatting.data.chattingRoom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.*
import com.example.justchatting.data.DTO.*
import com.example.justchatting.data.remote.NotificationAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChattingRoomFirebaseSource : KoinComponent {

    private val notificationAPI: NotificationAPI by inject()
    private var logArrayList: ArrayList<Message> = ArrayList()

    private var _chatLogs = MutableLiveData<ArrayList<Message>>()
    val logs: LiveData<ArrayList<Message>>
        get() = _chatLogs

    private var _newGroupId = MutableLiveData<String>()
    val newGroupId: LiveData<String>
        get() = _newGroupId

    private var _members = MutableLiveData<ArrayList<UserModel>>()
    val members: LiveData<ArrayList<UserModel>>
        get() = _members

    val uid = FirebaseAuth.getInstance().uid

    fun toArrayList(groupMembers: HashMap<String, UserModel>){
        var arrayList = ArrayList(groupMembers.values)
        arrayList.sortWith(Comparator { o1,o2->
            o1.username.compareTo(o2.username)
        })
        _members.postValue(arrayList)
    }

    fun setListener(groupId: String) {
        if(groupId == "")
            return

        val chatLogRef = FirebaseDatabase.getInstance().getReference("/messages/$groupId")
        chatLogRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(Message::class.java) ?: return

                logArrayList.add(chatMessage)
                _chatLogs.postValue(logArrayList)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    fun createGroupId(groupMembers: HashMap<String, UserModel>) {

        val groupId = FirebaseDatabase.getInstance().getReference("/chatrooms").push().key
        JustApp.roomId = groupId!!

        val membersRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
        membersRef.setValue(groupMembers).addOnCompleteListener {
            loadGroupMembers(groupMembers, groupId)

            groupMembers.forEach {
                val userGroupRef =
                    FirebaseDatabase.getInstance().getReference("/user_groups/${it.key}/$groupId")
                userGroupRef.setValue(createGroupName(groupMembers, it))
            }
            _newGroupId.postValue(groupId)
        }

        if(isAlone(groupMembers)){
            insertAloneChattingRoomId(uid!!, groupId)
        } else if(isOneToOne(groupMembers)){
            val friendId = getFriendId(groupMembers)
            insertAloneChattingRoomId(friendId, groupId)
            insertOneToOneChattingRoomId(friendId, groupId)
        }
    }

    private fun createGroupName(
        groupMembersMap: HashMap<String, UserModel>,
        member: Map.Entry<String, UserModel>
    ): String {
        val stringBuilder = StringBuilder()

        for (entry in groupMembersMap.entries) {
            if (member != entry || groupMembersMap.size == 1)
                stringBuilder.append(entry.value.username + ',')

            if (stringBuilder.length >= 19){
                break
            }
        }

        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        if(stringBuilder.length>=19){
            stringBuilder.append(" ...")
        }
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
        JustApp.roomId = groupId!!

        val membersRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
        membersRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(UserModel::class.java) ?: return
                groupMembers[user.uid] = user
                toArrayList(groupMembers)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java) ?: return
                groupMembers.remove(user.uid)
                toArrayList(groupMembers)
            }
        })
    }

    fun pushFCM(
        text: String,
        groupMembers: HashMap<String, UserModel>,
        groupId: String
    ): Completable {
        var notificationInfo =
            NotificationInfo(
                title = groupMembers[FirebaseAuth.getInstance().uid]!!.username,
                body = text,
                chatRoomId = groupId
            )
        var registrationIds = TokenParser()
            .parser(groupMembers)

        var notificationRequest =
            NotificationRequest(
                registrationIds,
                notificationInfo
            )

        return notificationAPI.pushMessage(notificationRequest)
    }

    fun addMember(
        member: java.util.HashMap<String, UserModel>,
        groupId: String
    ) {
        val membersRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
        membersRef.setValue(member).addOnCompleteListener {
            loadGroupMembers(member, groupId)

            member.forEach {
                val userGroupRef =
                    FirebaseDatabase.getInstance().getReference("/user_groups/${it.key}/$groupId")
                userGroupRef.setValue(createGroupName(member, it))
            }
        }

    }

    fun exit(groupId: String) {
        val uid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().getReference("/members/$groupId/$uid").removeValue()
        FirebaseDatabase.getInstance().getReference("/user_groups/$uid/$groupId").removeValue()

        FirebaseDatabase.getInstance().getReference("/members").child(groupId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount.toInt() == 0){
                    snapshot.ref.removeValue()
                    FirebaseDatabase.getInstance().getReference("/messages/$groupId").removeValue()
                    FirebaseDatabase.getInstance().getReference("/chatrooms/$groupId").removeValue()
                }
            }
        })

    }
    private fun isOneToOne(groupMembers: HashMap<String, UserModel>) = groupMembers.size == 2
    private fun isAlone(groupMembers: HashMap<String, UserModel>) = groupMembers.size == 1
    private fun insertAloneChattingRoomId(friendId : String, groupId: String){
        val fromRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
        fromRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Friend::class.java) ?: return
                val changeData = Friend(
                    data.isNotBlocked,
                    groupId!!
                )
                fromRef.setValue(changeData)
            }
        })
    }
    private fun insertOneToOneChattingRoomId(friendId : String, groupId: String){
        val toRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
        toRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Friend::class.java) ?: return
                val changeData =
                    Friend(
                        data.isNotBlocked,
                        groupId!!
                    )
                toRef.setValue(changeData)
            }
        })
    }

    private fun getFriendId(groupMembers: HashMap<String, UserModel>): String {
        var friendId = ""
        groupMembers.forEach {
            if (it.key != uid) {
                friendId = it.key
            }
        }
        return friendId
    }
}