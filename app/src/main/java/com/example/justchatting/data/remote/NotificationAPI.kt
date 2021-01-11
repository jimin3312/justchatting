package com.example.justchatting.data.remote

import com.example.justchatting.data.DTO.NotificationRequest
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val key = "AAAAlxwe_Tw:APA91bF8qia1fD5YjD2MLzuv1RXubuky_5USQ4Ct_gg7H3J-OkmsDwTdF7CDSbMe1Pswrffg153pgj8jRxXnE4shJb1sZN5VE2oYpJVzVryzkQaCJ6xWoOdm7oyjxQWE_Z82OY0y3Pnd"

interface NotificationAPI {
    @Headers(
        "Content-type: application/json",
        "Authorization: key=$key")
    @POST("fcm/send")
    fun pushMessage(@Body request: NotificationRequest) : Completable
}