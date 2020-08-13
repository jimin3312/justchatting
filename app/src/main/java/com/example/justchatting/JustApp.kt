package com.example.justchatting

import android.app.Application
import com.example.justchatting.di.FriendViewModel
import com.example.justchatting.di.SettingsViewModel
import com.example.justchatting.di.chattingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JustApp : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JustApp)
            modules(FriendViewModel,
                chattingViewModel,
                SettingsViewModel)
        }
    }
}