package com.example.justchatting.data.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Friend
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FriendFirebaseSource {
    
    private var _users : MutableLiveData<ArrayList<UserModel>> = MutableLiveData(ArrayList())
    val users : LiveData<ArrayList<UserModel>>
        get() = _users

    var myInfo: UserModel? = null

    private fun mapToList(friends: HashMap<String, UserModel>) : ArrayList<UserModel>{
        val arr = ArrayList(friends.values)
        arr.sortBy { it.username }
        if(myInfo != null) arr.add(0, myInfo)
        return arr
    }

    fun setFriendListChangeListener(friends : HashMap<String, UserModel>)
    {
        val myUid = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/friends/$myUid")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val friend = snapshot.getValue(Friend::class.java) ?: return
                if(friend.isNotBlocked) {
                    val friendId = snapshot.key?: return
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$friendId")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(UserModel::class.java)?:return
                            if(user.uid == myUid) myInfo = user
                            else friends[user.uid] = user
                            _users.postValue(mapToList(friends))
                        }
                    })
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }
}