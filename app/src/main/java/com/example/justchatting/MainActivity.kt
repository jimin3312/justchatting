package com.example.justchatting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.justchatting.databinding.ActivityMainBinding
import com.example.justchatting.ui.chatting.ChattingFragment
import com.example.justchatting.ui.friend.FriendFragment
import com.example.justchatting.ui.login.RegisterActivity
import com.example.justchatting.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var friendFragment : Fragment
    lateinit var chattingFragment : Fragment
    lateinit var settingFragment : Fragment
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            setPermission()
        }
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
            friendFragment = FriendFragment()
            chattingFragment = ChattingFragment()
            settingFragment = SettingsFragment()

            setListener()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, friendFragment).commit()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setPermission()
            } else {
                finish()
            }
        }
    }
}
