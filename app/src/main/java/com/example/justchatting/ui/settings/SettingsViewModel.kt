package com.example.justchatting.ui.settings

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.justchatting.repository.settings.SettingsRepository

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel(){

    var profileImage: Bitmap? = null
    var name : String = ""
    var email : String = ""
    fun loadMyProfileImage(){
        profileImage = settingsRepository.loadImage()
    }
    fun setNotificationConfig(boolean: Boolean){
        settingsRepository.setNotificationConfig(boolean)
    }

    fun uploadProfileImage(data: Uri?) {
        settingsRepository.uploadProfileImage(data)
    }
}