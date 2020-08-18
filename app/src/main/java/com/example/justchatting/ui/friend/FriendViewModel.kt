package com.example.justchatting.ui.friend

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.justchatting.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class FriendViewModel(application: Application) : AndroidViewModel(application), KoinComponent{

    private val userRepository : FriendUserRepository by inject()
    lateinit var myUser: LiveData<User>
    lateinit var users: LiveData<List<User>>

    fun getMyUser(){
        myUser = userRepository.getMyUser(getApplication())
    }

    fun getUsers() {
        users = userRepository.load(getApplication())
    }

    fun sync() {
        Log.d("FriendViewModel","sync start")
        userRepository.sync(getApplication())
        users = userRepository.load(getApplication())
        myUser = userRepository.getMyUser(getApplication())
    }
//    fun erase()
//    {
//        var eraseCount : Long = 0
//        val uId = FirebaseAuth.getInstance().uid
//        val ref = FirebaseDatabase.getInstance().getReference("/friends/$uId")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(error: DatabaseError) {}
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(currentSnapshot in snapshot.children)
//                {
//
//                    val partnerId = currentSnapshot.getValue(String::class.java)
//                    Log.d("fragErase", "partnerid: $partnerId")
//                    val partnerRef = FirebaseDatabase.getInstance().getReference("/friends/$partnerId")
//                    partnerRef.addListenerForSingleValueEvent(object : ValueEventListener{
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for(currentSnapshot in snapshot.children)
//                            {
//                                val id = currentSnapshot.getValue(String::class.java)
//                                Log.d("fragErase", "id: $id")
//                                if(id == uId) {
//                                    currentSnapshot.ref.removeValue().addOnSuccessListener {
//                                        eraseCount++
//                                        if( eraseCount == snapshot.childrenCount)
//                                        {
//                                            ref.removeValue()
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        override fun onCancelled(error: DatabaseError) {
//                        }
//                    })
//                }
//            }
//        })
//
//    }
}