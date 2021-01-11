package com.example.justchatting.data.DTO

import com.google.gson.annotations.SerializedName

data class NotificationInfo(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("chatRoomId") val chatRoomId: String = ""
)