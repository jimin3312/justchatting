package com.example.justchatting.di

import com.example.justchatting.repository.friend.FriendRepository
import org.koin.dsl.module

val friendModule = module {
    single { FriendRepository() }
//    factory { TabDialogFragment() }
//    factory { AddFriendFragment(get(),get()) }
}