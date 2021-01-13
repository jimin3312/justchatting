package com.example.justchatting.repository.settings

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import com.example.justchatting.Cache
import com.example.justchatting.data.auth.AuthFirebaseSource


class SettingsRepositoryImpl(private val cache: Cache, private val sharedPreferences: SharedPreferences, private val authFirebaseSource: AuthFirebaseSource) : SettingsRepository {
    override fun loadImage(): Bitmap? {
        return cache.loadBitmap("profileImage")
    }

    override fun setNotificationConfig(boolean: Boolean) {
        with (sharedPreferences.edit()) {
            putBoolean("notification", boolean)
            commit()
        }
    }

    override fun uploadProfileImage(data: Uri?) {
        authFirebaseSource.uploadProfileImage(data)
    }

}