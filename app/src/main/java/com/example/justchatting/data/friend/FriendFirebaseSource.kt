package com.example.justchatting.data.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.Friend
import com.example.justchatting.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FriendFirebaseSource {

    private var friendsMap : HashMap<String, UserModel> = HashMap()
    
    private var _users : MutableLiveData<ArrayList<UserModel>> = MutableLiveData(ArrayList())
    val users : LiveData<ArrayList<UserModel>>
        get() = _users

    var addFriend : MutableLiveData<Int> = MutableLiveData()

    var myInfo: UserModel? = null

    private fun mapToList() : ArrayList<UserModel>{
        val arr = ArrayList(friendsMap.values)
        arr.sortBy { it.username }
        if(myInfo != null) arr.add(0, myInfo)
        return arr
    }

    fun addFriendWithEmail(email : String){
        if(email.isEmpty())
            return

        val uid = FirebaseAuth.getInstance().uid
        val friendEmailRef = FirebaseDatabase.getInstance().getReference("/email/$email")

        friendEmailRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendId = snapshot.getValue(String()::class.java)
                if(friendId == null){
                    addFriend.postValue(-1)
                    return
                }

                if(friendsMap.containsKey(friendId)) {
                    addFriend.postValue(-2)
                    return
                }

                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(Friend(true, ""))
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(Friend(true, ""))

                addFriend.postValue(1)
            }
        })
    }

    fun addFriendWithPhoneNumber(phoneNum : String){
        if(phoneNum.isEmpty())
            return

        val uid = FirebaseAuth.getInstance().uid
        val friendPhoneRef = FirebaseDatabase.getInstance().getReference("/phone/$phoneNum")
        friendPhoneRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendId = snapshot.getValue(String::class.java)
                if(friendId == null){
                    addFriend.postValue(-1)
                    return
                }

                if(friendsMap.containsKey(friendId)) {
                    addFriend.postValue(-2)
                    return
                }

                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(Friend(true, ""))
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(Friend(true, ""))
                addFriend.postValue(1)
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
                            if(user.uid == uid) myInfo = user
                            else friendsMap[user.uid] = user
                            _users.postValue(mapToList())
                        }
                    })
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }
}