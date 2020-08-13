package com.example.kotlinmessenger.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.justchatting.MainActivity
import com.example.justchatting.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_eddittext_login.text.toString()

            Log.d("Login", "Attempt login with email/pw : $email/$password")

            auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    if(it.isSuccessful)
                    {
                        Log.d("Login", "success to login")
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else
                    {
                        Log.d("Login", "signInWithEmail:failure", it.exception)
                        Toast.makeText(baseContext, "Authentication failed.",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        back_to_register_button_login.setOnClickListener{
            finish()
        }
    }
}