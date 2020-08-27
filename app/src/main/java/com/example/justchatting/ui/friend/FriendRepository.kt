package com.example.justchatting.ui.friend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FriendRepository() {

    private var _friendMap : HashMap<String, User> = HashMap()
        val friendMap : HashMap<String, User>
            get()= _friendMap
    fun getFriends(): MutableLiveData<ArrayList<User>> {
        val friends = MutableLiveData<ArrayList<User>>()
        friends.postValue(ArrayList(_friendMap.values))
        return friends
    }
    fun loadFriends() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    if(snapshot.getValue(Boolean::class.java)!!)
                    {
                        val friendId = snapshot.key
                        val friendUserRef =  FirebaseDatabase.getInstance().getReference("/users/$friendId")
                        friendUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {
                            }
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user = snapshot.getValue(User::class.java)?: return
                                _friendMap[user.uid] = user
                            }
                        })
                    }
                }
            }
        })
    }

    fun makeFriendRelationships(contactList : ArrayList<String>){
        Log.d("FriendViewModel", "Sync start")
        var uid = FirebaseAuth.getInstance().uid
        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$uid")

        myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val myUser = snapshot.getValue(User::class.java)?: return
                Log.d("FriendViewModel:", "my username: ${myUser.username}")
                contactList.add(myUser.phoneNumber)

                contactList.forEachIndexed{ _, number->
                    val ref = FirebaseDatabase.getInstance().getReference("/phone/$number")
                    Log.d("REPO","number : $number")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {}
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.getValue(Boolean::class.java)!!)
                            {
                                val friendId = snapshot.key
                                val fromRef = FirebaseDatabase.getInstance().getReference("/friends/${uid}/${friendId}")
                                fromRef.setValue(true)
                                if(uid != friendId) {
                                    val toRef = FirebaseDatabase.getInstance().getReference("/friends/${friendId}/$uid")
                                    toRef.setValue(true)
                                }

                            }
                        }
                    })
                }
            }
        })
    }
    fun addFriendWithId(email : String){
        val uid = FirebaseAuth.getInstance().uid
        val friendEmailRef = FirebaseDatabase.getInstance().getReference("/email/$email")
        friendEmailRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendId = snapshot.key ?: return
                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(true)
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(true)
            }
        })
    }
    fun addFriendWithPhoneNumber(phoneNum : String){
        val uid = FirebaseAuth.getInstance().uid
        val friendPhoneRef = FirebaseDatabase.getInstance().getReference("/phone/$phoneNum")
        friendPhoneRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendId = snapshot.key ?: return
                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(true)
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(true)
            }
        })
    }
    fun setListener()
    {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uid")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                Log.d("setListener", "onChildChanged")
                if(snapshot.child("uid").exists() && snapshot.child("phoneNumber").exists() &&
                    snapshot.child("profileImageUrl").exists() && snapshot.child("username").exists() &&
                    snapshot.child("email").exists()) {
                    val user = snapshot.getValue(User::class.java) ?: return
                    _friendMap[user.uid] = user
                }
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                Log.d("setListener", "onChildChanged")
                if(snapshot.child("uid").exists() && snapshot.child("phoneNumber").exists() &&
                    snapshot.child("profileImageUrl").exists() && snapshot.child("username").exists() &&
                    snapshot.child("email").exists()) {
                    val user = snapshot.getValue(User::class.java) ?: return
                    _friendMap[user.uid] = user
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }
}