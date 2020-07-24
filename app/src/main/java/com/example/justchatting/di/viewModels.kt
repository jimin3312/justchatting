package com.example.justchatting.di

import com.example.justchatting.ui.chatting.ChattingViewModel
import com.example.justchatting.ui.friend.FriendViewModel
import com.example.justchatting.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chattingViewModel = module {
    viewModel { ChattingViewModel() }
}

val FriendViewModel = module {
    viewModel { FriendViewModel() }
}

val SettingsViewModel = module {
    viewModel { SettingsViewModel() }
}