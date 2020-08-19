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
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FriendUserRepository(private val userDao: UserDao){

    fun getUsers(myId : String) : LiveData<List<User>>{
        return userDao.getAll(myId)
    }

    fun getUserById(userId: String): LiveData<User>{
        return userDao.getUserById(userId)
    }

    fun insertUser(user: User): Completable {
        return Completable.fromCallable{userDao.insertUser(user)}
    }
    fun getAnyUser() : LiveData<User> {
        return userDao.getAnyUser()
    }
}