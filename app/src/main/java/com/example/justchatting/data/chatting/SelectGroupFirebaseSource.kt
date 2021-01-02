package com.example.justchatting.data.chatting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.UserModel
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SelectGroupFirebaseSource {

    private var friends : ArrayList<UserModel> = ArrayList()

    private var _users : MutableLiveData<ArrayList<UserModel>> = MutableLiveData(ArrayList())
    val users : LiveData<ArrayList<UserModel>>
        get() = _users

    var groupId = MutableLiveData<String>()

    fun loadGroupId(groupMembers: HashMap<String, UserModel>) {
        Log.d("멤버", "ㅁㅁㅁ")

        var find = false
        val uid = FirebaseAuth.getInstance().uid

        val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModel = dataSnapshot.getValue(UserModel::class.java)?: return
                groupMembers[uid!!]= userModel

                Log.d("그룸멤버2", groupMembers.toString())

                val ref = FirebaseDatabase.getInstance().getReference("/user_groups/$uid")

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot1: DataSnapshot) {

                        snapshot1.children.forEachIndexed{ index, userChatRoomId ->

                            val chatRoomMembersRef = FirebaseDatabase.getInstance().getReference("/members/${userChatRoomId.key}")
                            chatRoomMembersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {

                                }
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    val membersIdMap = snapshot.getValue(object : GenericTypeIndicator<HashMap<String, UserModel>>() {})

                                    if(find)
                                        return

                                    if(index.toLong() == snapshot1.childrenCount-1){
                                        if(membersIdMap != groupMembers) {
                                            groupId.postValue("")
                                        } else
                                            groupId.postValue(snapshot.key!!)

                                    } else {
                                        if (snapshot.childrenCount != groupMembers.size.toLong()) {
                                            return
                                        }
                                        if(membersIdMap != groupMembers)
                                            return
                                        else {
                                            find = true
                                            groupId.postValue(snapshot.key!!)
                                        }
                                    }

                                }
                            })
                        }
                    }
                })
            }
        })


    }

    fun loadFriends() {

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach{dataSnapshot ->
                    val isNotBlocked = dataSnapshot.getValue(Boolean::class.java)?: return
                    if(isNotBlocked)
                    {
                        val friendId = dataSnapshot.key
                        val friendUserRef =  FirebaseDatabase.getInstance().getReference("/users/$friendId")
                        friendUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {
                            }
                            override fun onDataChange(snapshot2: DataSnapshot) {
                                val user = snapshot2.getValue(UserModel::class.java)?: return
                                friends.add(user)
                                friends.sortBy { it.username }
                                _users.postValue(friends)
                            }
                        })
                    }
                }

            }
        })
    }

}