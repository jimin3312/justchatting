package com.example.justchatting.ui.friend

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toObservable
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val compositeDisposable : CompositeDisposable = CompositeDisposable()
    private val userRepository : FriendUserRepository by inject()
    private var myUserId = FirebaseAuth.getInstance().uid

    lateinit var myUser: Observable<User>
    lateinit var users : Observable<PagedList<User>>

    init {
        setListener()
    }
    fun loadMyUser(myId : String){
        myUser = userRepository.getMyUser(myId)
    }
    fun loadUsers() {
        users = userRepository.getUsers(myUserId!!).toObservable(pageSize = 30)
    }

    fun getAnyUser() : Single<User> {
        return userRepository.getAnyUser()
    }

    fun insert(user : User) {
        compositeDisposable.add(userRepository.insertUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("insert", "success")
            },{
                Log.d("insert", "fail")
            })
        )
    }

    fun sync(){

        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$myUserId")

        myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val myUser = snapshot.getValue(User::class.java)?: return
                Log.d("FriendViewModel:", "my username: ${myUser.username}")

                var contactList = getContacts(getApplication())
                contactList.add(myUser.phoneNumber)
                contactList.forEach{ number->

                    val ref = FirebaseDatabase.getInstance().getReference("/phone/$number")
                    Log.d("REPO","number : $number")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {}
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java) ?: return
                            Log.d("FriendViewModel uid", user.uid)
                            Log.d("FriendViewModel", user.username)

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

//    fun setUserDatabase()
//    {
//        val ref = FirebaseDatabase.getInstance().getReference("/friends/$myUserId")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {
//            }
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach{ child->
//                    val user = child.getValue(User::class.java)
//                    if(user != null)
//                        insert(user)
//                }
//            }
//        })
//    }
    private fun setListener()
    {
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$myUserId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.child("phoneNumber").exists() &&
                    snapshot.child("profileImageUrl").exists() &&
                    snapshot.child("uid").exists() &&
                    snapshot.child("username").exists())
                {
                    val user = snapshot.getValue(User::class.java) ?: return
                    insert(user)
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}