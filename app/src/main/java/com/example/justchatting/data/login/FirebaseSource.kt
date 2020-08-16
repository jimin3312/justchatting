package com.example.justchatting.data.login

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.core.Completable

class FirebaseSource {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun logout() = auth.signOut()

    fun loginWithEmail(email: String, password: String): Completable = Completable.create{ emitter ->
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(!emitter.isDisposed){
                if(it.isComplete)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

}