package com.example.justchatting.data.chatting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.ChattingRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChattingFirebaseSource {
    companion object{
        val TAG = "ChattingRepository"
    }
    private var chattingRoomMap = HashMap<String, ChattingRoom>()

    private var _chattingRooms : MutableLiveData<ArrayList<ChattingRoom>> = MutableLiveData()
    val chattingRooms : LiveData<ArrayList<ChattingRoom>>
        get() =  _chattingRooms

//    fun loadChatRooms() {
//        chattingRoomsList.clear()
//        val uid = FirebaseAuth.getInstance().uid
//        val userGroupRef = FirebaseDatabase.getInstance().getReference("/user_groups/$uid")
//        userGroupRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val cnt = snapshot.childrenCount.toInt()
//                snapshot.children.forEach { dataSnapshot ->
//                    Log.d(TAG,dataSnapshot.toString())
//                    val data = dataSnapshot.getValue(Boolean::class.java) ?: return
//                    if (data) {
//                        val groupId = dataSnapshot.key
//                        val chatRoomRef = FirebaseDatabase.getInstance().getReference("/chatrooms/$groupId")
//                        chatRoomRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onCancelled(error: DatabaseError) {
//                            }
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                val chattingModel = snapshot.getValue(ChattingModel::class.java) ?: return
//                                Log.d(TAG, "loadChat : ${chattingModel.lastMessage}")
//                                chattingRoomsList.add(chattingModel)
//
//                                if(cnt == chattingRoomsList.size){
//                                    chattingRoomsList.sortByDescending { it.timeStamp }
//                                    _chattingRooms.postValue(chattingRoomsList)
//                                }
//                            }
//                        })
//                    }
//                }
//
//            }
//        })
//    }


    fun setListener() {

        val uid = FirebaseAuth.getInstance().uid
        val userGroupRef = FirebaseDatabase.getInstance().getReference("/user_groups/$uid")
        userGroupRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot1: DataSnapshot, previousChildName: String?) {

                val groupId = snapshot1.key
                val chatRoomRef = FirebaseDatabase.getInstance().getReference("/chatrooms/$groupId")
                chatRoomRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chattingRoom = snapshot.getValue(ChattingRoom::class.java) ?: return

                        val userGroupNameRef = FirebaseDatabase.getInstance().getReference("/user_groups/$uid/$groupId")
                        userGroupNameRef.addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {
                            }
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val groupName = snapshot.getValue(String::class.java)?: return
                                chattingRoom.groupName = groupName
                                chattingRoomMap[groupId!!] = chattingRoom
                                _chattingRooms.postValue(getChattingArrayList())
                            }
                        })
                    }
                })
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    private fun getChattingArrayList(): ArrayList<ChattingRoom> {
        val arrayList = ArrayList(chattingRoomMap.values)
        arrayList.sortByDescending { it.timeStamp }
        return arrayList
    }

}