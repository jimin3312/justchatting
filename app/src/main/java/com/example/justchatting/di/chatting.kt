package com.example.justchatting.di

import com.example.justchatting.repository.chatting.ChattingRepository
import com.example.justchatting.repository.chatting.ChattingRepositoryImpl
import com.example.justchatting.repository.chatting.SelectGroupRepository
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import org.koin.dsl.module

val chattingModule = module{
    factory<SelectGroupRepository> { SelectGroupRepositoryImpl() }
    single<ChattingRepository>{ChattingRepositoryImpl()}
}