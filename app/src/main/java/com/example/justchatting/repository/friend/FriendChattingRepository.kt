package com.example.justchatting.repository.friend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendChattingRepository {
    var groupId = MutableLiveData<String>()

    fun loadGroupId(groupMembers: HashMap<String,Boolean>) {
        var find = false
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/user_groups/$uid")

        ref.addListenerForSingleValueEvent(object  : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { userChatRoomId->
                    Log.d(SelectGroupRepositoryImpl.TAG, "chatRoomId : ${userChatRoomId.key}")
                    val chatRoomMembersRef = FirebaseDatabase.getInstance().getReference("/members/${userChatRoomId.key}")
                    chatRoomMembersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }
                        override fun onDataChange(snapshot: DataSnapshot) {

                            Log.d(SelectGroupRepositoryImpl.TAG, snapshot.childrenCount.toString())
                            Log.d(SelectGroupRepositoryImpl.TAG, groupMembers.size.toString())
                            if( find || snapshot.childrenCount != groupMembers.size.toLong()){
                                return
                            }
                            snapshot.children.forEach { dataSnapshot ->
                                Log.d(SelectGroupRepositoryImpl.TAG,dataSnapshot.key)
                                if(!groupMembers.containsKey(dataSnapshot.key)){
                                    return
                                }
                            }
                            find = true
                            groupId.postValue(snapshot.key!!)
                        }
                    })
                }
                if(!find){
                    groupId.postValue("")
                }
            }
        })
    }
}