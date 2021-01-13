package com.example.justchatting

import android.app.Application
import com.example.justchatting.di.*
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin



class JustApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        startKoin {
            androidContext(this@JustApp)
            modules(listOf(viewModelModule, friendModule, loginModule, chattingModule, networkModule, chattingRoomModule, cacheModule,settingsModule))
        }
    }

    companion object{
         var roomId: String= ""
    }
}