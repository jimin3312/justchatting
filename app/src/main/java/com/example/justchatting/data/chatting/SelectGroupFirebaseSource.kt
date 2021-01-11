package com.example.justchatting.data.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Event
import com.example.justchatting.data.DTO.Friend
import com.example.justchatting.data.DTO.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SelectGroupFirebaseSource {

    private var friends: ArrayList<UserModel> = ArrayList()

    private var _users: MutableLiveData<ArrayList<UserModel>> = MutableLiveData(ArrayList())
    val users: LiveData<ArrayList<UserModel>>
        get() = _users

    var groupId = MutableLiveData<Event<String>>()
    val uid = FirebaseAuth.getInstance().uid

    fun loadGroupId(groupMembers: HashMap<String, UserModel?>) {

        val userRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModel = dataSnapshot.getValue(UserModel::class.java) ?: return
                groupMembers[uid!!] = userModel

                when {
                    isAlone(groupMembers) -> {
                        fetchGroupId(uid)
                    }
                    isOneToOne(groupMembers) -> {
                        fetchGroupId(getFriendId(groupMembers))
                    }
                    else -> {
                        groupId.postValue(Event(""))
                    }
                }
            }
        })
    }

    fun loadFriends(alreadyEnteredMember: HashMap<String, UserModel>) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach { dataSnapshot ->
                    val friend = dataSnapshot.getValue(Friend::class.java) ?: return
                    if (friend.isNotBlocked) {
                        val friendId = dataSnapshot.key
                        val friendUserRef =
                            FirebaseDatabase.getInstance().getReference("/users/$friendId")
                        friendUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                            }

                            override fun onDataChange(snapshot2: DataSnapshot) {
                                val user = snapshot2.getValue(UserModel::class.java) ?: return

                                if (user.uid == uid || alreadyEnteredMember.containsKey(user.uid))
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

    private fun getFriendId(groupMembers: HashMap<String, UserModel?>): String {
        var friendId = ""
        groupMembers.forEach {
            if (it.key != uid) {
                friendId = it.key
            }
        }
        return friendId
    }

    private fun fetchGroupId(friendId: String) {
        val friendRef =
            FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
        friendRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val friend = snapshot.getValue(Friend::class.java) ?: return
                groupId.postValue(Event(friend.groupId))
            }
        })
    }

    private fun isOneToOne(groupMembers: HashMap<String, UserModel?>) = groupMembers.size == 2
    private fun isAlone(groupMembers: HashMap<String, UserModel?>) = groupMembers.size == 1
}