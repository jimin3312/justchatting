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
        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$myUserId")

            myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val myUser = snapshot.getValue(User::class.java) ?: return
                    runBlocking {
                        userDao.insertUser(myUser)
                    }
                    setListener()
                }
            })

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
        executor.execute {
            var contactList = getContacts(application)
            val usersRef = FirebaseDatabase.getInstance().getReference("/users/")
            val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$myUserId")

            myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    val myUser = snapshot.getValue(User::class.java) ?: return
                    Log.d("FriendViewModel", "myUser : ${myUser.username}")
                    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (currentSnapshot in snapshot.children) {
                                val user = currentSnapshot.getValue(User::class.java) ?: return
                                if (user.uid == myUserId) {
                                    Log.d("FriendViewModel", "add Me : ${myUser.username}")
                                    continue
                                }
                                Log.d("FriendViewModel", "username: ${user.username}")
                                contactList.forEach {
                                    if (it == user.phoneNumber) {
                                        val fromRef = FirebaseDatabase.getInstance()
                                            .getReference("/friends/$myUserId/${user.uid}")
                                        val toRef = FirebaseDatabase.getInstance()
                                            .getReference("/friends/${user.uid}/$myUserId")
                                        fromRef.setValue(user)
                                        toRef.setValue(myUser)
                                        runBlocking {
                                            userDao.insertUser(user)
                                        }
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })

                }
            })
        }
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
                        val phoneNo: String = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
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
        val uId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java) ?: return

                Log.d("FriendFragment", "onChildChanged : ${user.username}")
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java) ?: return

                Log.d("FriendFragment", "onChildAdded : ${user.username}")
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }
}