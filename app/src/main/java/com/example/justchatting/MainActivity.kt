package com.example.justchatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.justchatting.ui.chatting.ChattingFragment
import com.example.justchatting.ui.friend.FriendFragment
import com.example.justchatting.ui.login.RegisterActivity
import com.example.justchatting.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var friendFragment : Fragment
    lateinit var chattingFragment : Fragment
    lateinit var settingFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            friendFragment = FriendFragment()
            chattingFragment = ChattingFragment()
            settingFragment = SettingsFragment()

            setListener()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, friendFragment).commit()
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
}
