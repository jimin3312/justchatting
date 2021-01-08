package com.example.justchatting.ui.chattingRoom

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.view.GravityCompat
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
import com.example.justchatting.ui.chatting.SelectGroupActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_chatting_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val REQUEST_CODE = 1000

class ChattingRoomActivity : BaseActivity<ActivityChattingRoomBinding>() {

    private val viewModel : ChattingRoomViewModel by viewModel()
    private var groupId : String? = null
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration
    private val friendsAdapter = FriendsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)

        binding.navView.getHeaderView(0).findViewById<RecyclerView>(R.id.chatting_room_drawer_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = friendsAdapter
        }
        binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.chatting_room_drawer_add_member).setOnClickListener {
            val intent = Intent(this, SelectGroupActivity::class.java)
            intent.putExtra("members", viewModel.groupMembers)
            intent.putExtra("before","chattingRoomActivity")
            startActivityForResult(intent, REQUEST_CODE)
        }
        binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.chtting_room_drawer_exit).setOnClickListener {
            Log.d("나가기","나가기")
            viewModel.exit()
        }

        groupId = intent.getStringExtra("groupId")
        Log.d("그룹아이디", groupId)
        if(groupId == ""){
            viewModel.groupMembers = intent.getSerializableExtra("groupMembers") as HashMap<String, UserModel>
            friendsAdapter.setUsers(mapToArrayList(viewModel.groupMembers))
            friendsAdapter.notifyDataSetChanged()
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

        viewModel.getMembers().observe(this, Observer {
            friendsAdapter.setUsers(it)
            friendsAdapter.notifyDataSetChanged()
        })

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null ){
            val invitedMember = data.getSerializableExtra("invited member") as HashMap<String, UserModel>
            viewModel.groupMembers.putAll(invitedMember)

            if(viewModel.groupId == ""){
                friendsAdapter.setUsers(mapToArrayList(viewModel.groupMembers))
                friendsAdapter.notifyDataSetChanged()
            }else {
                viewModel.addMember()
            }
        }
    }
    private fun mapToArrayList(hashMap: HashMap<String, UserModel>): ArrayList<UserModel> {
        var arrayList = ArrayList(hashMap.values)
        arrayList.sortBy { it.username }
        return arrayList
    }
}