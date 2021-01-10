package com.example.justchatting.data.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Friend
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendAdditionFirebaseSource {
    private var _isValidToAdd : MutableLiveData<Int> = MutableLiveData()
    val isValidToAdd : MutableLiveData<Int>
        get() = _isValidToAdd

    fun addFriendWithEmail(email : String, friends: HashMap<String, UserModel>) {
        if(email.isEmpty())
            return

        val uid = FirebaseAuth.getInstance().uid
        val friendEmailRef = FirebaseDatabase.getInstance().getReference("/email/$email")

        friendEmailRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendId = snapshot.getValue(String()::class.java)
                if(friendId == null){
                    _isValidToAdd.postValue(-1)
                    return
                }

                if(friends.containsKey(friendId)) {
                    _isValidToAdd.postValue(-2)
                    return
                }

                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(Friend(true, ""))
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(Friend(true, ""))

                _isValidToAdd.postValue(1)
            }
        })
    }

    fun addFriendWithPhoneNumber(phoneNum : String, friends: HashMap<String, UserModel>){
        if(phoneNum.isEmpty())
            return

        val uid = FirebaseAuth.getInstance().uid
        val friendPhoneRef = FirebaseDatabase.getInstance().getReference("/phone/$phoneNum")
        friendPhoneRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendId = snapshot.getValue(String::class.java)
                if(friendId == null){
                    _isValidToAdd.postValue(-1)
                    return
                }

                if(friends.containsKey(friendId)) {
                    _isValidToAdd.postValue(-2)
                    return
                }

                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(Friend(true, ""))
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(Friend(true, ""))
                _isValidToAdd.postValue(1)
            }
        })
    }
}