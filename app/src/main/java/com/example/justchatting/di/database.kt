package com.example.justchatting.di

import androidx.room.Room
import com.example.justchatting.ui.friend.UserDatabase
import com.example.justchatting.ui.friend.FriendUserRepository
import com.example.justchatting.ui.friend.FriendViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val databaseModule = module{
    single { Room.databaseBuilder(get(), UserDatabase::class.java,"user_db").build()}
    single { get<UserDatabase>().userDao() }
    single { FriendUserRepository(get(), Executors.newSingleThreadExecutor()) }
}