package com.example.justchatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.justchatting.databinding.ActivityMainBinding
import com.example.justchatting.ui.login.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyUserIsLoggedIn()
    }
    private fun verifyUserIsLoggedIn() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
            val navController = findNavController(R.id.nav_host_fragment)
            binding.bottomNavigationView.setupWithNavController(navController)
        }
    }
}
