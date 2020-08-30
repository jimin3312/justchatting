package com.example.justchatting.di

import com.example.justchatting.ui.friend.FriendRepository
import com.example.justchatting.ui.friend.dialog.AddFriendFragment
import com.example.justchatting.ui.friend.dialog.TabDialogFragment
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val friendModule = module {
    single { FriendRepository() }
//    factory { TabDialogFragment() }
//    factory { AddFriendFragment(get(),get()) }
}