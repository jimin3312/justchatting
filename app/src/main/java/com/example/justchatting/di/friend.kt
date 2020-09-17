package com.example.justchatting.di

import com.example.justchatting.repository.friend.FriendChattingRepository
import com.example.justchatting.repository.friend.FriendRepository
import org.koin.dsl.module

val friendModule = module {
    single { FriendRepository() }
    factory { FriendChattingRepository() }
}