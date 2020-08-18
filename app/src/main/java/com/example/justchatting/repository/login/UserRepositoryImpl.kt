package com.example.justchatting.repository.login

import android.net.Uri
import com.example.justchatting.data.login.FirebaseSource
import io.reactivex.rxjava3.core.Completable

class UserRepositoryImpl(
    private val firebaseSource: FirebaseSource
) : UserRepository {

    override fun loginWithEmail(email: String, password: String) = firebaseSource.loginWithEmail(email, password)

    override fun signUpWithEmail(email: String, password: String) = firebaseSource.signUp(email, password)

    override fun uploadProfile(uri: Uri?) = firebaseSource.uploadProfile(uri)

    override fun saveUser(name: String, phoneNumber: String, selectedPhotoUri: Uri?) = firebaseSource.saveUser(name, phoneNumber, selectedPhotoUri)

    override fun logout() = firebaseSource.logout()
}