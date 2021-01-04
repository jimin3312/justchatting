package com.example.justchatting

import com.google.gson.annotations.SerializedName

data class NotificationRequest(
    @SerializedName("registration_ids") val registration_ids : List<String>,
    @SerializedName("data") val data: NotificationInfo

)