package com.example.justchatting.di

import com.example.justchatting.data.chattingRoom.ChattingRoomFirebaseSource
import com.example.justchatting.data.remote.NotificationAPI
import com.example.justchatting.repository.chatting.SelectGroupRepository
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepository
import com.example.justchatting.repository.chattingRoom.ChattingRoomRepositoryImpl
import org.koin.dsl.module

val chattingRoomModule = module {
    factory { ChattingRoomFirebaseSource() }
    factory<ChattingRoomRepository> {ChattingRoomRepositoryImpl(get())}
}