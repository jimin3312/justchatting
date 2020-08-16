package com.example.justchatting.repository.login

import com.example.justchatting.data.login.FirebaseSource

class UserRepositoryImpl(
    private val firebaseSource: FirebaseSource
) : UserRepository {

    override fun loginWithEmail(email: String, pass: String) = firebaseSource.loginWithEmail(email, pass)

    override fun logout() = firebaseSource.logout()
}