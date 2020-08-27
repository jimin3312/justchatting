package com.example.justchatting.data.login

import android.net.Uri
import android.util.Log
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

class FirebaseSource {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun logout() = auth.signOut()

    fun loginWithEmail(email: String, password: String): Completable = Completable.create{ emitter ->
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
        }
    }


    fun signUp(email: String, password: String): Single<String> = Single.create{ emitter ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                emitter.onSuccess("")
            }
            .addOnFailureListener{
                emitter.onError(it)
            }
    }


    fun uploadProfile(uri: Uri?): Single<String> {

        return Single.create{emitter ->
            if(uri == null)
                emitter.onSuccess("")

            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/profileImages/$filename")
            ref.putFile(uri!!)
                .addOnSuccessListener {
                    Log.d("Register", "Successfully uploaded image: ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity", "File Location : $it")
                        emitter.onSuccess(it.toString())
                    }
                }.addOnCanceledListener {
                    emitter.onError(Exception("upload is canceled"))
                }.addOnFailureListener{
                    emitter.onError(it)
                }
        }
    }

    fun saveUser(name: String, phoneNumber: String, firebaseImageResourcePath: String, email: String): Completable = Completable.create{emitter ->
        val uid = FirebaseAuth.getInstance().uid ?: ""

        val re = Regex("[^A-Za-z0-9 ]")
        val email = re.replace(email,"")

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
            uid,
            name,
            phoneNumber,
            firebaseImageResourcePath,
            email
        )


        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebase database")
                val phoneRef = FirebaseDatabase.getInstance().getReference("/phone/$phoneNumber")
                phoneRef.setValue(user).addOnSuccessListener {
                    val emailRef = FirebaseDatabase.getInstance().getReference("/email/$email")
                    emailRef.setValue(user).addOnSuccessListener {
                        emitter.onComplete()
                    }
                }
            }
            .addOnFailureListener{
                emitter.onError(it)
            }

    }
}