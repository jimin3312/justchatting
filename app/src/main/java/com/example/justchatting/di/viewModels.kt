package com.example.justchatting.di

import com.example.justchatting.ui.chatting.ChattingViewModel
import com.example.justchatting.ui.friend.FriendViewModel
import com.example.justchatting.ui.login.LoginVIewModel
import com.example.justchatting.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ChattingViewModel() }
    viewModel { FriendViewModel(androidApplication())}
    viewModel { SettingsViewModel() }
    viewModel { LoginVIewModel(get()) }
}