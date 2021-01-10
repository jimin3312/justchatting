package com.example.justchatting.di

import com.example.justchatting.data.friend.ContactsDAO
import com.example.justchatting.data.friend.FriendAdditionFirebaseSource
import com.example.justchatting.data.friend.FriendFirebaseSource
import com.example.justchatting.repository.friend.FriendRepository
import com.example.justchatting.repository.friend.FriendRepositoryImpl
import org.koin.dsl.module

val friendModule = module {
    factory { FriendFirebaseSource() }
    factory { ContactsDAO() }
    factory { FriendAdditionFirebaseSource() }
    factory<FriendRepository> { FriendRepositoryImpl(get(), get(), get()) }
}