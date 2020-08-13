package com.example.justchatting.ui.friend

import android.app.Application
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FriendViewModel(application: Application) : AndroidViewModel(application){
    private  val usersMutableLiveData = MutableLiveData<ArrayList<User>>()

    var arrayList= ArrayList<User>()
    init
    {

        val uId = FirebaseAuth.getInstance().uid
        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$uId")

        myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val myUser = snapshot.getValue(User::class.java)?:return
                arrayList.add(0, myUser)
                usersMutableLiveData.postValue(arrayList)
                setListener()
            }
        })
    }
    fun setListener()
    {
        val uId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uId")

        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java) ?: return


//                usersMutableLiveData.value?.forEach {
//                    Log.d("FriendFragment", "onChildChanged : ${it.username}")
//                    arrayList.add(it)
//                }
                Log.d("FriendFragment", "onChildChanged : ${user.username}")
                arrayList.add(user)
                usersMutableLiveData.postValue(arrayList)
            }
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java) ?: return

//                usersMutableLiveData.value?.forEach {
//                    Log.d("FriendFragment", "onChildAdded : ${it.username}")
//                    arrayList.add(it)
//                }
                Log.d("FriendFragment", "onChildAdded : ${user.username}")
                arrayList.add(user)
                usersMutableLiveData.postValue(arrayList)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    fun getUsers(): MutableLiveData<ArrayList<User>> {
        return usersMutableLiveData
    }
    fun sync()
    {
        Log.d("FriendViewModel","sync start")
        val contactList = ArrayList<String>()
        val cr = getApplication<Application>().contentResolver
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
                        val phoneNo: String = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contactList.add(phoneNo)
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()

        val uId = FirebaseAuth.getInstance().uid ?: return
        val usersRef = FirebaseDatabase.getInstance().getReference("/users/")
        val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$uId")

//        arrayList = ArrayList()
//        myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//            }
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val myUser = snapshot.getValue(User::class.java)?:return
//                arrayList.add(0,myUser)
//            }
//        })

        myUserRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val myUser = snapshot.getValue(User::class.java)?:return
                Log.d("FriendViewModel","myUser : ${myUser.username}")
                usersRef.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(currentSnapshot in snapshot.children)
                        {
                            val user = currentSnapshot.getValue(User::class.java)?:return
                            if(user.uid == uId)
                            {
                                Log.d("FriendViewModel","add Me : ${myUser.username}")
//                                arrayList.add(0, myUser)
                                continue
                            }
                            Log.d("FriendViewModel","username: ${user.username}")
                            contactList.forEach{
                                if(it == user.phoneNumber )
                                {
                                    val fromRef = FirebaseDatabase.getInstance().getReference("/friends/$uId/${user.uid}")
                                    val toRef = FirebaseDatabase.getInstance().getReference("/friends/${user.uid}/$uId")
                                    fromRef.setValue(user)
                                    toRef.setValue(myUser)
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
    fun erase()
    {
        var eraseCount : Long = 0
        val uId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for(currentSnapshot in snapshot.children)
                {

                    val partnerId = currentSnapshot.getValue(String::class.java)
                    Log.d("fragErase", "partnerid: $partnerId")
                    val partnerRef = FirebaseDatabase.getInstance().getReference("/friends/$partnerId")
                    partnerRef.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(currentSnapshot in snapshot.children)
                            {
                                val id = currentSnapshot.getValue(String::class.java)
                                Log.d("fragErase", "id: $id")
                                if(id == uId) {
                                    currentSnapshot.ref.removeValue().addOnSuccessListener {
                                        eraseCount++
                                        if( eraseCount == snapshot.childrenCount)
                                        {
                                            ref.removeValue()
                                        }
                                    }
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
            }
        })

    }
}