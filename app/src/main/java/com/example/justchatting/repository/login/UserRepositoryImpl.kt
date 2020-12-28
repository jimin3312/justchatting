package com.example.justchatting.repository.login

import android.net.Uri
import com.example.justchatting.data.Auth.AuthFirebaseSource

class UserRepositoryImpl(
    private val authFirebaseSource: AuthFirebaseSource
) : UserRepository {

    override fun loginWithEmail(email: String, password: String) = authFirebaseSource.loginWithEmail(email, password)

    override fun signUpWithEmail(email: String, password: String) = authFirebaseSource.signUp(email, password)

    override fun uploadProfile(uri: Uri?) = authFirebaseSource.uploadProfile(uri)

    override fun saveUser(
        name: String,
        phoneNumber: String,
        firebaseImageResourcePath: String,
        email: String
    ) = authFirebaseSource.saveUser(name, phoneNumber, firebaseImageResourcePath, email)

    override fun logout() = authFirebaseSource.logout()
}