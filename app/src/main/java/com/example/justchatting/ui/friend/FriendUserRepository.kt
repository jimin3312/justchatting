package com.example.justchatting.ui.friend

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.justchatting.User
import io.reactivex.rxjava3.core.Completable

class FriendUserRepository(private val userDao: UserDao){

    fun getUsers(myId : String) : DataSource.Factory<Int, User>{
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