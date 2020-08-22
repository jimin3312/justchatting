package com.example.justchatting.ui.friend

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.justchatting.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM users_tb Where uid  NOT IN (:myId) ORDER BY user_name ASC")
    fun getAll(myId : String) : DataSource.Factory<Int, User>

    @Query("SELECT * FROM users_tb WHERE uid IN (:userId)")
    fun getUserById(userId: String): LiveData<User>

    @Query("SELECT * FROM users_tb LIMIT 1")
    fun getAnyUser() : Single<User>

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User) :Completable

    @Update
    fun update(user : User) : Completable



}