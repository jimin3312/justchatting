package com.example.justchatting.di

import com.example.justchatting.BuildConfig
import com.example.justchatting.data.remote.NotificationAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
private const val CONNECT_TIMEOUT = 15L
private const val WRITE_TIMEOUT = 15L
private const val READ_TIMEOUT = 15L


val networkModule = module {


    single { GsonBuilder().create() }

    single(createdAtStart = false) { get<Retrofit>().create(NotificationAPI::class.java) }

    single {
        OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            retryOnConnectionFailure(false)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
        }.build()
    }

    single {
        Retrofit.Builder().apply {
            baseUrl("https://fcm.googleapis.com")
            addConverterFactory(GsonConverterFactory.create(get()))
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(get())
        }.build()
    }
}