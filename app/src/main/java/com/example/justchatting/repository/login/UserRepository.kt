package com.example.justchatting.repository.login

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun loginWithEmail(email: String, password: String) : Completable
    fun logout()
    fun signUpWithEmail(email: String, password: String): Completable
    fun uploadProfile(uri: Uri?): Single<String>
    fun saveUser(
        name: String,
        phoneNumber: String,
        firebaseImageResourcePath: String,
        email: String
    ): Single<Boolean>

    fun updateToken(): Completable
}
