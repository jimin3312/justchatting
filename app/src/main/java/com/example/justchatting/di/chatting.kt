package com.example.justchatting.di

import com.example.justchatting.repository.chatting.SelectGroupRepository
import com.example.justchatting.repository.chatting.SelectGroupRepositoryImpl
import org.koin.dsl.module

val chattingModuel = module{
    single<SelectGroupRepository> { SelectGroupRepositoryImpl() }
}