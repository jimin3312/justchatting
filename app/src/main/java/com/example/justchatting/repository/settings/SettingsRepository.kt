package com.example.justchatting.repository.settings

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.Single

interface SettingsRepository {
    fun setNotificationConfig(boolean: Boolean)
    fun uploadProfileImage(data: Uri?) : Single<String>
    fun getNotificationConfig(): Boolean?
    fun loadImage(context: Context): Bitmap?
    fun saveProfileImageToCache(context: Context, bitmap: Bitmap)
    fun editProfileImageUrl(url: String)
}