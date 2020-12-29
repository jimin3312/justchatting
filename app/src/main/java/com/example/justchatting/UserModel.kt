package com.example.justchatting

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    val uid : String ="",
    val username : String ="",
    val phoneNumber: String= "",
    val profileImageUrl : String? = null,
    val email : String = "",
    val token : String = ""
) : Parcelable

