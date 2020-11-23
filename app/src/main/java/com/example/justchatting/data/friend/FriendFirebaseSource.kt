package com.example.justchatting.data.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private lateinit var myInfo : UserModel

    var addFriend : MutableLiveData<Int> = MutableLiveData()

    private fun mapToList() : ArrayList<UserModel>{
        val arrayList = ArrayList(friendsMap.values)
        arrayList.sortBy { it.username }
        arrayList.add(0, myInfo)
        return arrayList
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

                friendsMap.forEach{
                    if(it.key == friendId){
                        addFriend.postValue(-2)
                        return
                    }
                }
                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(true)
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(true)
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
                val friendId = snapshot.getValue(String()::class.java)
                if(friendId == null){
                    addFriend.postValue(-1)
                    return
                }
                val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                fromUserFriendRef.setValue(true)
                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                toUserFriendRef.setValue(true)
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

                val isNotBlocked = snapshot.getValue(Boolean::class.java) ?: return
                if(isNotBlocked) {
                    val friendId = snapshot.key?: return
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$friendId")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(UserModel::class.java)?:return
                            friendsMap[user.uid] = user
                            _users.postValue(mapToList())
                        }
                    })
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    fun loadMyInfo() {
        val uid = FirebaseAuth.getInstance().uid
        val myRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val my = snapshot.getValue(UserModel::class.java) ?: return
                myInfo = my
                _users.postValue(mapToList())
            }
        })
    }
}