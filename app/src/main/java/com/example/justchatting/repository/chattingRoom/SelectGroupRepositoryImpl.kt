package com.example.justchatting.repository.chattingRoom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SelectGroupRepositoryImpl : SelectGroupRepository {

    private val _friends : MutableLiveData<ArrayList<User>> = MutableLiveData()
    val friends : LiveData<ArrayList<User>>
        get() = _friends

    override fun loadFriends() {
        val uid = FirebaseAuth.getInstance().uid
        val friendsRef = FirebaseDatabase.getInstance().getReference("/friends/$uid")
        val friendList = ArrayList<User>()
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
                                val user = snapshot.getValue(User::class.java)?: return
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
}