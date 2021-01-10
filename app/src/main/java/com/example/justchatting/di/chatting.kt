package com.example.justchatting.di

import com.example.justchatting.data.chatting.ChattingFirebaseSource
import com.example.justchatting.data.chatting.SelectGroupFirebaseSource
import com.example.justchatting.repository.chatting.ChattingRepository
import com.example.justchatting.repository.chatting.ChattingRepositoryImpl
import com.example.justchatting.repository.chatting.SelectGroupRepository
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import org.koin.dsl.module

val chattingModule = module {
    factory { SelectGroupFirebaseSource() }
    factory { ChattingFirebaseSource() }
    factory<SelectGroupRepository> { SelectGroupRepositoryImpl(get()) }
    factory<ChattingRepository> { ChattingRepositoryImpl(get()) }
}