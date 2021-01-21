package com.example.justchatting.repository.settings

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import com.example.justchatting.Cache
import com.example.justchatting.data.auth.AuthFirebaseSource
import io.reactivex.Single


class SettingsRepositoryImpl(private val cache: Cache, private val sharedPreferences: SharedPreferences, private val authFirebaseSource: AuthFirebaseSource) : SettingsRepository {
    override fun loadImage(context: Context): Bitmap? {
        return cache.loadBitmap(context)
    }

    override fun saveProfileImageToCache(context: Context, bitmap: Bitmap){
        cache.saveBitmap(context, bitmap)
    }

    override fun editProfileImageUrl(url: String) {
        authFirebaseSource.editProfileImageUrl(url)
    }

    override fun getNotificationConfig(): Boolean? {
        return sharedPreferences.getBoolean("notification", true)
    }

    override fun setNotificationConfig(boolean: Boolean) {
        with (sharedPreferences.edit()) {
            putBoolean("notification", boolean)
            commit()
        }
    }

    override fun uploadProfileImage(data: Uri?): Single<String> {
        return authFirebaseSource.uploadProfileImage(data)
    }


}