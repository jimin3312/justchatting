package com.example.justchatting.repository.chatting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SelectGroupRepositoryImpl : SelectGroupRepository {

    companion object{
        val TAG = "SelectGroupRepo"
    }
    private var _groupId = MutableLiveData<String>()
    val groupId : LiveData<String>
        get() = _groupId

    private val _friends : MutableLiveData<ArrayList<UserModel>> = MutableLiveData()
    val friends : LiveData<ArrayList<UserModel>>
        get() = _friends

    override fun loadFriends() {
        val uid = FirebaseAuth.getInstance().uid
        val friendsRef = FirebaseDatabase.getInstance().getReference("/friends/$uid")
        val friendList = ArrayList<UserModel>()
        friendsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{ dataSnapshot->
                    val isFriend = dataSnapshot.getValue(Boolean::class.java) ?: return
                    if(isFriend) {
                        val friendId = dataSnapshot.key
                        val friendRef = FirebaseDatabase.getInstance().getReference("/users/$friendId")
                        friendRef.addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {
                            }
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user = snapshot.getValue(UserModel::class.java)?: return
                                Log.d("ChattingRoomRepo", "username : ${user.username}")
                                friendList.add(user)
                                friendList.sortBy { data->
                                    data.username
                                }
                                _friends.postValue(friendList)
                            }
                        })
                    }
                }
            }
        })
    }

    override fun loadGroupId(groupMembers: HashMap<String,Boolean>) {
    var find = false
    val uid = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/user_groups/$uid")

    ref.addListenerForSingleValueEvent(object  : ValueEventListener{
        override fun onCancelled(error: DatabaseError) {

        }
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach { userChatRoomId->
                Log.d(TAG, "chatRoomId : ${userChatRoomId.key}")
                val chatRoomMembersRef = FirebaseDatabase.getInstance().getReference("/members/${userChatRoomId.key}")
                chatRoomMembersRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {

                    }
                    override fun onDataChange(snapshot: DataSnapshot) {

                        Log.d(TAG, snapshot.childrenCount.toString())
                        Log.d(TAG, groupMembers.size.toString())
                        if( find || snapshot.childrenCount != groupMembers.size.toLong()){
                            return
                        }
                        snapshot.children.forEach { dataSnapshot ->
                            Log.d(TAG,dataSnapshot.key)
                            if(!groupMembers.containsKey(dataSnapshot.key)){
                                return
                            }
                        }
                        find = true
                        _groupId.postValue(snapshot.key!!)
                    }
                })
            }
            if(!find){
                _groupId.postValue("")
            }
        }
    })
    }


}