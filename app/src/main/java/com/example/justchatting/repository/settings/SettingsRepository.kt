package com.example.justchatting.repository.settings

import android.graphics.Bitmap
import android.net.Uri

interface SettingsRepository {
    fun loadImage() : Bitmap?
    fun setNotificationConfig(boolean: Boolean)
    fun uploadProfileImage(data: Uri?)
}