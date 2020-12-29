package com.example.justchatting

import com.google.gson.annotations.SerializedName

data class NotificationInfo (
    @SerializedName("title") val title : String,
    @SerializedName("body") val body : String
)