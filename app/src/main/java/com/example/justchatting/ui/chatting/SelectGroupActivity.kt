package com.example.justchatting.ui.chatting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.R
import com.example.justchatting.UserModel
import com.example.justchatting.databinding.ActivitySelectGroupBinding
import com.example.justchatting.ui.chattingRoom.ChattingRoomActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_select_group.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectGroupActivity : AppCompatActivity() {
    companion object {
        val TAG = "SelectGroupActivity"
    }

    lateinit var binding: ActivitySelectGroupBinding
    lateinit var menuItem: MenuItem
    val viewModel: SelectGroupViewModel by viewModel()
    lateinit var selectGroupRecyclerviewAdapter: SelectGroupRecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_group)

        viewModel.addAlreadyEnteredMember(intent.getSerializableExtra("members") as? HashMap<String, UserModel>)

        selectGroupRecyclerviewAdapter = SelectGroupRecyclerviewAdapter()
        select_group_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = selectGroupRecyclerviewAdapter
        }

        viewModel.load()
        viewModel.getFriends().observe(this, Observer {
            selectGroupRecyclerviewAdapter.setFriendList(it)
            selectGroupRecyclerviewAdapter.notifyDataSetChanged()
        })

        selectGroupRecyclerviewAdapter.checkedCnt.observe(this, Observer { cnt ->
            menuItem.isEnabled = cnt > 0
        })

        viewModel.getGroupId().observe(this, Observer { groupId ->
            val intent = Intent(this, ChattingRoomActivity::class.java)
            intent.putExtra("groupId", groupId)
            intent.putExtra("groupMembers", selectGroupRecyclerviewAdapter.groupMembers)
            startActivity(intent)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if(intent.getStringExtra("before")!= null){
            menuInflater.inflate(R.menu.selcect_menu_from_chatting_room, menu)
            menuItem = menu!!.findItem(R.id.select_invite)
        } else {
            menuInflater.inflate(R.menu.select_menu, menu)
            menuItem = menu!!.findItem(R.id.select_confirm)
        }
        menuItem.isEnabled = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.select_confirm -> {
                viewModel.loadGroupId(selectGroupRecyclerviewAdapter.groupMembers)
            }
            R.id.select_invite -> {
                intent.putExtra("invited member",selectGroupRecyclerviewAdapter.groupMembers)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}