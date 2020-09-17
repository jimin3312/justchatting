package com.example.justchatting.repository.chattingRoom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.ChatMessageModel
import com.example.justchatting.ChattingModel
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.GenericTypeIndicator as get


class ChattingRoomRepositoryImpl : ChattingRoomRepository {

    private var chatLogArrayList : ArrayList<ChatMessageModel> = ArrayList()
    private var groupNameList : ArrayList<String> = ArrayList()

    private var _chatLogs = MutableLiveData<ArrayList<ChatMessageModel>>()
    val chatLogsModel : LiveData<ArrayList<ChatMessageModel>>
    get() = _chatLogs

    private var _newGroupId = MutableLiveData<String>()
    val newGroupId : LiveData<String>
    get() = _newGroupId

    companion object{
        val TAG = "ChattingRoomRepo"
    }

    override fun loadGroupNameList(groupMembersMap: HashMap<String, Boolean> ){
        val uid = FirebaseAuth.getInstance().uid

        if(groupMembersMap.size==2) {
            groupMembersMap.remove(uid)
        }
        val arrayList: ArrayList<String> = ArrayList(groupMembersMap.keys)
        groupMembersMap[uid!!] = true
        arrayList.sortBy { it }
        arrayList!!.forEach{
            Log.d(TAG,"key : ${it}")
            val userRef = FirebaseDatabase.getInstance().getReference("/users/${it}")
            userRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)?:return
                    Log.d(TAG,"username :${user.username}")
                    groupNameList.add(user.username)
                }
            })
        }
            Log.d(TAG,"membersName : $groupNameList")

    }

    override fun fetchChatLog(groupId : String) {
        chatLogArrayList = ArrayList()
        val chatLogRef = FirebaseDatabase.getInstance().getReference("/messages/$groupId").orderByChild("timestamp")
        chatLogRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {dataSnapshot->
                    val chatMessage = dataSnapshot.getValue(ChatMessageModel::class.java) ?: return
                    chatLogArrayList.add(chatMessage)
                    _chatLogs.postValue(chatLogArrayList)
                }
            }
        })
    }
    override fun setListener(groupId : String){
        val chatLogRef = FirebaseDatabase.getInstance().getReference("/messages/$groupId")
        chatLogRef.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG,"Listener onChildAdd")
                val chatMessage = snapshot.getValue(ChatMessageModel::class.java) ?: return
                Log.d(TAG,"chat : ${chatMessage.text}")

                chatLogArrayList.add(chatMessage)
                _chatLogs.postValue(chatLogArrayList)

                val memberRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
                memberRef.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {

                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val membersIdMap = snapshot.getValue(object : get<HashMap<String, Boolean>>() {})
                        Log.d(TAG,"membersIdMap : $membersIdMap")

                        var chattingModel : ChattingModel
                        chattingModel = if(chatMessage.type=="text")
                            ChattingModel(groupNameList , membersIdMap!!, chatMessage.text,chatMessage.timeStamp, groupId)
                        else
                            ChattingModel(groupNameList, membersIdMap!!, "사진",chatMessage.timeStamp, groupId)
                        val chatRoomRef = FirebaseDatabase.getInstance().getReference("/chatrooms/$groupId")
                        chatRoomRef.setValue(chattingModel)
                    }
                })
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    override fun makeRelationship(groupMembersMap: HashMap<String, Boolean>) {
        val groupId = FirebaseDatabase.getInstance().getReference("/chatrooms").push().key
        val membersRef = FirebaseDatabase.getInstance().getReference("/members/$groupId")
        membersRef.setValue(groupMembersMap).addOnCompleteListener {
            groupMembersMap.forEach{
                val userGroupRef = FirebaseDatabase.getInstance().getReference("/user_groups/${it.key}/$groupId")
                userGroupRef.setValue(true)
            }
            _newGroupId.postValue(groupId)
        }
    }

    override fun sendText(text: String, groupId: String) {
        val uid = FirebaseAuth.getInstance().uid
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        userRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)?: return
                var messageRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("/messages/$groupId").push()
                val chatMessage = ChatMessageModel("text",messageRef.key.toString(),uid!!,user.profileImageUrl!!,"",text,  System.currentTimeMillis())
                messageRef.setValue(chatMessage)
            }
        })
    }
}