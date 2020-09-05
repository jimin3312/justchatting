package com.example.justchatting.di

import com.example.justchatting.repository.chattingRoom.SelectGroupRepository
import com.example.justchatting.repository.chattingRoom.SelectGroupRepositoryImpl
import org.koin.dsl.module

val chattingRoomModule = module {
    factory<SelectGroupRepository> { SelectGroupRepositoryImpl() }
}