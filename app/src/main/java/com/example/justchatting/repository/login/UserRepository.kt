package com.example.justchatting.repository.login

import android.net.Uri
import io.reactivex.Completable

interface UserRepository {
    fun loginWithEmail(email: String, pass: String) : Completable
    fun logout()
    fun signUpWithEmail(email: String, password: String): Completable
    fun uploadProfile(uri: Uri?): Completable
    fun saveUser(name: String, phoneNumber: String, selectedPhotoUri: Uri?): Completable
}
