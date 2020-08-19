package com.example.justchatting.ui.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.justchatting.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users_tb Where uid  NOT IN (:myId) ORDER BY user_name ASC")
    fun getAll(myId : String) : LiveData<List<User>>

    @Query("SELECT * FROM users_tb WHERE uid IN (:userId)")
    fun getUserById(userId: String): LiveData<User>

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User)

    @Query("DELETE FROM users_tb WHERE uid = (:uId)")
    fun delete(uId: String)

    @Query("SELECT * FROM users_tb LIMIT 1")
    fun getAnyUser() : LiveData<User>
}