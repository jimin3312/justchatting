package com.example.justchatting.di

import com.example.justchatting.data.login.FirebaseSource
import com.example.justchatting.repository.login.UserRepository
import com.example.justchatting.repository.login.UserRepositoryImpl
import org.koin.dsl.module

val loginModule = module {
    single { FirebaseSource() }
    factory<UserRepository> { UserRepositoryImpl(get()) }
}