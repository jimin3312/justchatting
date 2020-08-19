package com.example.justchatting.data.login

import android.net.Uri
import android.util.Log
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.rxjava3.core.Completable
import java.util.*

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


    fun signUp(email: String, password: String): Completable = Completable.create{ emitter ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                emitter.onComplete()
            }
            .addOnFailureListener{
                emitter.onError(it)
            }
    }

    fun uploadProfile(uri: Uri?): Completable {
        uri ?: return Completable.complete()

        return Completable.create{emitter ->
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(uri)
                .addOnSuccessListener {
                    Log.d("Register", "Successfully uploaded image: ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity", "File Location : $it")
                        emitter.onComplete()
                    }
                }.addOnCanceledListener {
                    emitter.onError(Exception("upload is canceled"))
                }.addOnFailureListener{
                    emitter.onError(it)
                }
        }
    }

    fun saveUser(name: String, phoneNumber: String, selectedPhotoUri: Uri?): Completable = Completable.create{emitter ->
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
            uid,
            name,
            phoneNumber,
            selectedPhotoUri.toString()
        )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebase database")
                val ref = FirebaseDatabase.getInstance().getReference("/phone/$phoneNumber")
                ref.setValue(user).addOnSuccessListener {
                    emitter.onComplete()
                }
            }
            .addOnFailureListener{
                emitter.onError(it)
            }

    }
}