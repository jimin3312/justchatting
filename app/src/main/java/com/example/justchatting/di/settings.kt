package com.example.justchatting.di

import com.example.justchatting.repository.settings.SettingsRepository
import com.example.justchatting.repository.settings.SettingsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val PREF = "notification"
val settingsModule = module{
    factory<SettingsRepository> { SettingsRepositoryImpl(get(), androidContext().getSharedPreferences(PREF, 0), get()) }
}