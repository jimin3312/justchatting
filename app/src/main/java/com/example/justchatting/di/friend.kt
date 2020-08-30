package com.example.justchatting.di

import com.example.justchatting.ui.friend.FriendRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val friendModule = module {
    single { FriendRepository() }
}