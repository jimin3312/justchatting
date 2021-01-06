package com.example.justchatting.ui.chattingRoom

import android.os.Bundle
import android.util.Log
import android.view.*
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
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.UserModel
import com.example.justchatting.base.BaseActivity
import com.example.justchatting.databinding.ActivityChattingRoomBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_chatting_room.*
import kotlinx.android.synthetic.main.chatting_room_drawer_header.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChattingRoomActivity : BaseActivity<ActivityChattingRoomBinding>() {

    private val viewModel : ChattingRoomViewModel by viewModel()
    private var groupId : String? = null
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)

        groupId = intent.getStringExtra("groupId")
        Log.d("그룹아이디", groupId)
        if(groupId == ""){
            viewModel.groupMembers = intent.getSerializableExtra("groupMembers") as HashMap<String, UserModel>
        } else {
            viewModel.groupId = groupId!!
            viewModel.loadGroupMembers(groupId)
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        Log.d("구성원",ArrayList(viewModel.groupMembers.values).toString())
        val friendsAdapter = FriendsAdapter()
        friendsAdapter.setUsers(ArrayList(viewModel.groupMembers.values))
        nav_view.getHeaderView(0).findViewById<RecyclerView>(R.id.chatting_room_drawer_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = friendsAdapter
        }
    }

    override fun getLayoutId() = R.layout.activity_chatting_room

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.chatting_room_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.chatting_room_drawer_summon -> {
                drawerLayout.openDrawer(GravityCompat.END)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}