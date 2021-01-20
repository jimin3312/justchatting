package com.example.justchatting.di

import com.example.justchatting.Cache
import org.koin.dsl.module

val cacheModule = module{
    factory { Cache() }
}