package com.example.justchatting.data.DTO

import com.example.justchatting.data.DTO.NotificationInfo
import com.google.gson.annotations.SerializedName

data class NotificationRequest(
    @SerializedName("registration_ids") val registration_ids : List<String>,
    @SerializedName("data") val data: NotificationInfo
)