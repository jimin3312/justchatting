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
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FriendUserRepository(private val userDao: UserDao, private val executor :Executor ){

    var myUserId = FirebaseAuth.getInstance().uid?: ""

    init
    {
        setListener()
    }

    fun getMyUser(application: Application) : LiveData<User> {
        if(userDao.getAnyUser().value == null)
            sync(application)
        return userDao.getUserById(myUserId)
    }
    fun load(application: Application) : LiveData<List<User>>{
        if(userDao.getAnyUser().value == null)
            sync(application)
        return userDao.getAll(myUserId)
    }

    fun sync(application: Application){

        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$myUserId")

        myUserRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val myUser = snapshot.getValue(User::class.java)?: return
                Log.d("FriendViewModel:", "my username: ${myUser.username}")

                var contactList = getContacts(application)
                contactList.forEach{ number->
                    val ref = FirebaseDatabase.getInstance().getReference("/phone/$number")
                    Log.d("REPO","number : $number")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {}
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java) ?: return
                            Log.d("FriendRepoSync uid", user.uid)
                            Log.d("FriendRepoSync", user.username)

                            val fromRef = FirebaseDatabase.getInstance().getReference("/friends/${myUserId}/${user.uid}")
                            fromRef.setValue(user)

                            if(user.uid != myUserId) {
                                val toRef = FirebaseDatabase.getInstance().getReference("/friends/${user.uid}/$myUserId")
                                toRef.setValue(myUser)
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
                    val pCur: Cursor? = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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

    fun setListener()
    {
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$myUserId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                if(snapshot.child("phoneNumber").exists() &&
                        snapshot.child("profileImageUrl").exists() &&
                        snapshot.child("uid").exists() &&
                        snapshot.child("username").exists()) {

                    val user = snapshot.getValue(User::class.java) ?: return
                    Log.d("FriendRepoChanged", user.username)
                    executor.execute {
                        userDao.insertUser(user)
                    }
                    Log.d("FriendFragment", "onChildChanged : ${user.username}")
                }
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.child("phoneNumber").exists() &&
                    snapshot.child("profileImageUrl").exists() &&
                    snapshot.child("uid").exists() &&
                    snapshot.child("username").exists())
                {
                    val user = snapshot.getValue(User::class.java) ?: return
                    Log.d("FriendRepoADD", user.username)
                    executor.execute {
                        userDao.insertUser(user)
                    }
                    Log.d("FriendFragment", "onChildAdded : ${user.username}")
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }
}