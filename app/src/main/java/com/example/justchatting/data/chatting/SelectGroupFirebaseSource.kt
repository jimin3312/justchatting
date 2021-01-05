package com.example.justchatting.data.chatting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Friend
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

        val uid = FirebaseAuth.getInstance().uid

        val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModel = dataSnapshot.getValue(UserModel::class.java)?: return
                groupMembers[uid!!]= userModel

                if(groupMembers.size <=2){

                    var friendId = ""
                    groupMembers.forEach{
                        if(it.key != uid){
                            friendId = it.key
                        }
                    }
                    if(friendId =="")
                        friendId = uid


                    val friendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                    friendRef.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val friend = snapshot.getValue(Friend::class.java)?: return
                            groupId.postValue(friend.groupId)
                        }
                    })
                } else{
                    groupId.postValue("")
                }
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
                    val friend = dataSnapshot.getValue(Friend::class.java)?: return
                    if(friend.isNotBlocked)
                    {
                        val friendId = dataSnapshot.key
                        val friendUserRef =  FirebaseDatabase.getInstance().getReference("/users/$friendId")
                        friendUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {
                            }
                            override fun onDataChange(snapshot2: DataSnapshot) {
                                val user = snapshot2.getValue(UserModel::class.java)?: return

                                if(user.uid == uid)
                                    return

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