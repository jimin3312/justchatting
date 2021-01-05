package com.example.justchatting.ui.chattingRoom

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.R
import com.example.justchatting.UserModel
import com.example.justchatting.base.BaseActivity
import com.example.justchatting.databinding.ActivityChattingRoomBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_chatting_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChattingRoomActivity : BaseActivity<ActivityChattingRoomBinding>() {

    private val viewModel : ChattingRoomViewModel by viewModel()
    private var groupId : String? = null
    private lateinit var chattingRoomAdapter: ChattingLogdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

        groupId = intent.getStringExtra("groupId")
        if(groupId == ""){
            viewModel.groupMembers = intent.getSerializableExtra("groupMembers") as HashMap<String, UserModel>
        } else {
            viewModel.loadGroupMembers(groupId)
        }

        chattingRoomAdapter = ChattingLogdapter()

        chatting_room_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = chattingRoomAdapter
        }

        if(groupId != ""){
            viewModel.setListener(groupId!!)
        }

        viewModel.getChatLogs().observe(this, Observer {
            chattingRoomAdapter.setChattingLog(it)
            chattingRoomAdapter.notifyDataSetChanged()
            chatting_room_recyclerview.scrollToPosition(it.size-1)
        })

        chatting_room_button_send.setOnClickListener {
            if(chatting_room_edittext_input.text.isNotEmpty()){
                if(groupId == ""){
                    viewModel.createGroupId()
                } else{
                    viewModel.sendText(chatting_room_edittext_input.text.toString(), groupId!!)
                    chatting_room_edittext_input.setText("")
                }
            }
        }

        viewModel.getNewGroupId().observe(this , Observer {newId->
            groupId = newId
            viewModel.setListener(groupId!!)
            viewModel.sendText(chatting_room_edittext_input.text.toString(), groupId!!)
            chatting_room_edittext_input.setText("")
        })

    }

    override fun getLayoutId() = R.layout.activity_chatting_room

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.chatting_room_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.chatting_room_drawer_summon -> {
                drawerLayout.openDrawer(GravityCompat.END)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}