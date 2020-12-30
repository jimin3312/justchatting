package com.example.justchatting

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.justchatting.data.remote.NotificationAPI
import com.example.justchatting.databinding.ActivityMainBinding
import com.example.justchatting.ui.chatting.ChattingFragment
import com.example.justchatting.ui.friend.FriendFragment
import com.example.justchatting.ui.login.LoginActivity
import com.example.justchatting.ui.login.RegisterActivity
import com.example.justchatting.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var friendFragment : Fragment
    lateinit var chattingFragment : Fragment
    lateinit var settingFragment : Fragment
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            setToken()
            setPermission()
        }
    }

    private fun setToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().uid}").child("token").setValue(it.result)
        }
    }

    private fun setPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this , Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            createFragment()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createFragment()
            } else {
                finish()
            }
        }
    }

    private fun createFragment(){
        friendFragment = FriendFragment()
        chattingFragment = ChattingFragment()
        settingFragment = SettingsFragment()

        setListener()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, friendFragment).commit()
    }

    private fun setListener() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.friend-> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, friendFragment).commit()
                        return true
                    }
                    R.id.chatting-> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, chattingFragment).commit()
                        return true
                    }
                    R.id.settings->{
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, settingFragment).commit()
                        return true
                    }
                }
                return false
            }
        })
    }
}