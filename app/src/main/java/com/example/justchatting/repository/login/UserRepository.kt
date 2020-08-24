package com.example.justchatting.repository.login

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {
    fun loginWithEmail(email: String, pass: String) : Completable
    fun logout()
    fun signUpWithEmail(email: String, password: String): Single<String>
    fun uploadProfile(uri: Uri?): Single<String>
    fun saveUser(
        name: String,
        phoneNumber: String,
        firebaseImageResourcePath: String,
        email: String
    ): Completable
}
