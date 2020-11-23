package com.example.justchatting.di

import com.example.justchatting.data.login.AuthFirebaseSource
import com.example.justchatting.repository.login.UserRepository
import com.example.justchatting.repository.login.UserRepositoryImpl
import org.koin.dsl.module

val loginModule = module {
    single { AuthFirebaseSource() }
    factory<UserRepository> { UserRepositoryImpl(get()) }
}