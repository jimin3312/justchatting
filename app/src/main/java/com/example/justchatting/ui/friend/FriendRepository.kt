package com.example.justchatting.ui.friend

import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FriendRepository {
    companion object{
        val TAG = "FriendRepository"
    }

    private var friendMap : HashMap<String, User> = HashMap()

    private var _friendUsers : MutableLiveData<ArrayList<User>> = MutableLiveData(ArrayList())
    val friendUsers : LiveData<ArrayList<User>>
        get() = _friendUsers

    private var _myUser : MutableLiveData<User> = MutableLiveData()
    val myUser : LiveData<User>
        get() = _myUser

    private var _addFriend : MutableLiveData<Boolean> = MutableLiveData()
    val addFriend : LiveData<Boolean>
        get() = _addFriend

    fun loadMyUser()
    {
        val uid = FirebaseAuth.getInstance().uid
        val myRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val my = snapshot.getValue(User::class.java) ?: return
                _myUser.postValue(my)
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
                    val data = dataSnapshot.getValue(Boolean::class.java)?: return
                    if(data)
                    {
                        val friendId = dataSnapshot.key
                        Log.d(TAG,"friendId : ${friendId}")
                        val friendUserRef =  FirebaseDatabase.getInstance().getReference("/users/$friendId")
                        friendUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {
                            }
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user = snapshot.getValue(User::class.java)?: return
                                Log.d(TAG,"user : ${user.username}")
                                friendMap[friendId!!] = user
                            }
                        })
                    }
                }
                _friendUsers.postValue(ArrayList(friendMap.values))
            }
        })
    }

    fun makeFriendRelationships(application: Application){
        Log.d(TAG, "Sync start")
        var uid = FirebaseAuth.getInstance().uid
        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$uid")

        myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val myUser = snapshot.getValue(User::class.java)?: return
                Log.d(TAG, "my username: ${myUser.username}")

                val contactList = getContacts(application)
                contactList.forEachIndexed { _, number ->
                    val ref = FirebaseDatabase.getInstance().getReference("/phone/$number")
                    Log.d(TAG, "number : $number")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {}
                        override fun onDataChange(snapshot: DataSnapshot) {
                            Log.d(
                                TAG,
                                "makeFriendRelation : ${snapshot.getValue(String::class.java)}"
                            )
                            val friendId = snapshot.getValue(String::class.java) ?: return
                            val fromRef = FirebaseDatabase.getInstance()
                                .getReference("/friends/${uid}/${friendId}")
                            fromRef.setValue(true)
                            if (uid != friendId) {
                                val toRef = FirebaseDatabase.getInstance()
                                    .getReference("/friends/${friendId}/$uid")
                                toRef.setValue(true)
                            }
                        }
                    })
                }
            }
        })
    }
    private fun getContacts(application: Application): ArrayList<String> {
        val contactList = ArrayList<String>()
        val cr = application.contentResolver
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        val temp : String = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        var phoneNo : String = ""
                        temp.forEach {
                            if(it.isDigit())
                                phoneNo+=it
                        }
                        contactList.add(phoneNo)
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()
        return contactList
    }

    fun addFriendWithId(email : String){
        val uid = FirebaseAuth.getInstance().uid
        val friendEmailRef = FirebaseDatabase.getInstance().getReference("/email/$email")
        friendEmailRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val isEmailExist = snapshot.getValue(Boolean::class.java)
                if(isEmailExist == null){
                    _addFriend.postValue(false)
                    return
                }
                if(isEmailExist) {
                    val friendId = snapshot.key
                    val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                    fromUserFriendRef.setValue(true)
                    val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                    toUserFriendRef.setValue(true)
                    _addFriend.postValue(true)
                }
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
                val isPhoneNumExist = snapshot.getValue(Boolean::class.java)
                if(isPhoneNumExist == null){
                    _addFriend.postValue(false)
                    return
                }
                if(isPhoneNumExist) {
                    val friendId = snapshot.key
                    val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$uid/$friendId")
                    fromUserFriendRef.setValue(true)
                    val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$friendId/$uid")
                    toUserFriendRef.setValue(true)
                    _addFriend.postValue(true)
                }
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

                val friend = snapshot.getValue(Boolean::class.java) ?: return
                if(friend)
                {
                    val friendId = snapshot.key?: return
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$friendId")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)?:return
                            Log.d(TAG, "onChildAdded ${user.username}")
                            friendMap[friendId] = user
                            _friendUsers.postValue(ArrayList(friendMap.values))
                        }
                    })
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }


}