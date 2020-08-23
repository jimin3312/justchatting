package com.example.justchatting.ui.friend

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toObservable
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Observable

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers

import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val compositeDisposable : CompositeDisposable = CompositeDisposable()
    private val userRepository : FriendUserRepository by inject()
    private var myUserId = FirebaseAuth.getInstance().uid

    private var myUser: Observable<User> = userRepository.getMyUser(myUserId!!)
    private var users : Observable<PagedList<User>> = userRepository.getUsers(myUserId!!).toObservable(pageSize = 30)

    private var isAddFriend = MutableLiveData<Boolean>()

    init {
        Log.d("FriendViewModel", "init")
        setListener()
    }
    fun getIsAddFriend() : MutableLiveData<Boolean>{
        return isAddFriend
    }
    fun getMyUser() : Observable<User>{
        return myUser
    }
    fun getUsers() : Observable<PagedList<User>>{
        return users
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
        Log.d("FriendViewModel", "Sync start")
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

    fun setUsersDatabase()
    {
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$myUserId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{ child->
                    val user = child.getValue(User::class.java)
                    if(user != null)
                        insert(user)
                }
            }
        })
    }
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
                if(snapshot.child("uid").exists() && snapshot.child("phoneNumber").exists() &&
                    snapshot.child("profileImageUrl").exists() && snapshot.child("username").exists() &&
                    snapshot.child("email").exists()) {
                    val user = snapshot.getValue(User::class.java) ?: return
                    insert(user)
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    fun addFriendWithId(email : String){
        val toUserRef = FirebaseDatabase.getInstance().getReference("/email/$email")
        toUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val toUser = snapshot.getValue(User::class.java)
                if(toUser==null) {
                    isAddFriend.postValue(false)
                    return
                }
                val fromUserRef = FirebaseDatabase.getInstance().getReference("/users/$myUserId")
                fromUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val fromUser = snapshot.getValue(User::class.java)
                        if(fromUser==null) {
                            isAddFriend.postValue(false)
                            return
                        }
                        val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$myUserId/${toUser.uid}")
                        fromUserFriendRef.setValue(toUser).addOnCompleteListener {
                            if(it.isSuccessful) {
                                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/${toUser.uid}/$myUserId")
                                toUserFriendRef.setValue(fromUser).addOnCompleteListener {
                                    if(it.isSuccessful) {
                                        Log.d("FriendViewModel", "add")
                                        isAddFriend.postValue(true)
                                    }
                                }
                            }
                        }
                    }
                })
            }
        })
    }
    fun addFriendWithPhoneNumber(toPhoneNum : String){
        Log.d("PhoneNumber : ", toPhoneNum)
        val toUserRef = FirebaseDatabase.getInstance().getReference("/phone/$toPhoneNum")
        toUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val toUser = snapshot.getValue(User::class.java)
                if(toUser==null) {
                    isAddFriend.postValue(false)
                    return
                }
                val fromUserRef = FirebaseDatabase.getInstance().getReference("/users/$myUserId")
                fromUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val fromUser = snapshot.getValue(User::class.java)
                        if(fromUser==null) {
                            isAddFriend.postValue(false)
                            return
                        }
                        val fromUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/$myUserId/${toUser.uid}")
                        fromUserFriendRef.setValue(toUser).addOnCompleteListener {
                            if(it.isSuccessful) {
                                val toUserFriendRef = FirebaseDatabase.getInstance().getReference("/friends/${toUser.uid}/$myUserId")
                                toUserFriendRef.setValue(fromUser).addOnCompleteListener {
                                    if(it.isSuccessful) {
                                        Log.d("FriendViewModel","add")
                                        isAddFriend.postValue(true)
                                    }
                                }
                            }
                        }
                    }
                })
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}