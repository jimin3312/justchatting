package com.example.justchatting.ui.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.justchatting.MainActivity
import com.example.justchatting.R
import com.example.justchatting.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.collections.ArrayList


class RegisterActivity : AppCompatActivity() {
    companion object{
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 1000
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()
        }

        phone_edittext_register.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        aleady_have_account_textview_register.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login activity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_photo_button_register.setOnClickListener {
            Log.d("RegisterActivity", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }
    }
    var selectedPhotoUri : Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null )
        {
            Log.d("RegisterActivity", "Photo was selected")
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            select_photo_imageview_register.setImageBitmap(bitmap)
            select_photo_button_register.alpha = 0f
        }
    }
    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        val username = username_edittext_register.text.toString()
        val phoneNumber = phone_edittext_register.text.toString()

        if(email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty())
        {
            Toast.makeText(this, "Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email  : $email")
        Log.d("RegisterActivity", "Password : $password")
        Log.d("RegisterActivity", "username : $username")
        Log.d("RegisterActivity", "phoneNum : $phoneNumber")


        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d("RegisterActivity", "create user with uid : ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()

            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Failed to create user : ${it.message}")
                Toast.makeText(this, "${it.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File Location : $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
            uid,
            username_edittext_register.text.toString(),
            phone_edittext_register.text.toString(),
            profileImageUrl
        )
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebase database")

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Failed to set value to database")
            }

        val phoneRef = FirebaseDatabase.getInstance().getReference("/phone/")
    }
}