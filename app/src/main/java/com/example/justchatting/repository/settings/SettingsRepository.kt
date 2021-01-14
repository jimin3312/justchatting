package com.example.justchatting.repository.settings

import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.Single

interface SettingsRepository {
    fun loadImage() : Bitmap?
    fun setNotificationConfig(boolean: Boolean)
    fun uploadProfileImage(data: Uri?) : Single<String>
    fun saveProfileImageToCache(bitmap: Bitmap)
    fun getNotificationConfig(): Boolean?
}