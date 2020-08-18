package com.example.justchatting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_tb")
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "user_name") val username : String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "profile_image_url") val profileImageUrl : String?
){
    constructor(): this("","","","")
}