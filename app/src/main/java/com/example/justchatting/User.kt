package com.example.justchatting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val username : String ="",
    val phoneNumber: String= "",
    val profileImageUrl : String? = null,
    val email : String = ""
)