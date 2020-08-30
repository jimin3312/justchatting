package com.example.justchatting.ui.friend

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendRepository(): KoinComponent {
    companion object{
        val TAG = "FriendRepository"
    }

    private var _friendMap : HashMap<String, User> = HashMap()
    val friendMap : HashMap<String, User>
        get() = _friendMap

    var friendUsers : MutableLiveData<ArrayList<User>> = MutableLiveData(ArrayList())
    var myUser : MutableLiveData<User> = MutableLiveData()
    var isFinished : MutableLiveData<Boolean> = MutableLiveData()
    var isFinished2 : MutableLiveData<Boolean> = MutableLiveData()

    fun loadMyUser()
    {
        val uid = FirebaseAuth.getInstance().uid
        val myRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val my = snapshot.getValue(User::class.java) ?: return
                myUser.postValue(my)
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
                                _friendMap[friendId!!] = user
                            }
                        })
                    }
                }
                friendUsers.postValue(ArrayList(_friendMap.values))
                Log.d("friendList", ArrayList(_friendMap.values).toString())
                isFinished2.postValue(true)
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
                isFinished.postValue(true)
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
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val isFriend = snapshot.getValue(Boolean::class.java) ?: return
                if(isFriend)
                {
                    val friendId = snapshot.key?: return
                    val ref = FirebaseDatabase.getInstance().getReference("/users/$friendId")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)?:return
                            Log.d(TAG, "onChildAdded ${user.username}")
                            _friendMap[friendId] = user
                            friendUsers.postValue(ArrayList(_friendMap.values))
                            isFinished2.postValue(true)
                        }
                    })
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }


}