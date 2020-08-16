package com.example.justchatting.repository.login

import io.reactivex.rxjava3.core.Completable

interface UserRepository {
    fun loginWithEmail(email: String, pass: String) : Completable
    fun logout()
}
