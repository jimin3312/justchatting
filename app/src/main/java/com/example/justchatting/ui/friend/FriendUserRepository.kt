package com.example.justchatting.ui.friend

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.justchatting.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class FriendUserRepository(private val userDao: UserDao){

    fun getUsers(myId : String) : DataSource.Factory<Int, User>{
        return userDao.getAll(myId)
    }

    fun getMyUser(myId: String): Observable<User>{
        return userDao.getMyUser(myId)
    }

    fun insertUser(user: User): Completable {
        return userDao.insertUser(user)
    }
    fun getAnyUser() : Single<User> {
        return userDao.getAnyUser()
    }
}