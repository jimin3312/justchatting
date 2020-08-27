package com.example.justchatting

import android.app.Application
import com.example.justchatting.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JustApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JustApp)
            modules(viewModelModule, databaseModule, loginModule, chattingRoomModule )
        }
    }
}