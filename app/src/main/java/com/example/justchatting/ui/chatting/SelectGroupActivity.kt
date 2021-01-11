package com.example.justchatting.ui.chatting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.justchatting.R
import com.example.justchatting.data.DTO.UserModel
import com.example.justchatting.base.BaseActivity
import com.example.justchatting.databinding.ActivitySelectGroupBinding
import com.example.justchatting.ui.chattingRoom.ChattingRoomActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectGroupActivity : BaseActivity<ActivitySelectGroupBinding>() {

    lateinit var menuItem: MenuItem
    val viewModel: SelectGroupViewModel by viewModel()
    lateinit var selectGroupRecyclerviewAdapter: SelectGroupRecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_group)

        viewModel.addAlreadyEnteredMember(intent.getSerializableExtra(resources.getString(R.string.alreadyEnteredMember)) as? HashMap<String, UserModel>)

        selectGroupRecyclerviewAdapter = SelectGroupRecyclerviewAdapter()

        binding.selectGroupRecyclerview.apply {
            setHasFixedSize(true)
            adapter = selectGroupRecyclerviewAdapter
        }

        viewModel.loadFriends()
        viewModel.getFriends().observe(this, Observer {
            selectGroupRecyclerviewAdapter.setFriendList(it)
            selectGroupRecyclerviewAdapter.notifyDataSetChanged()
        })

        selectGroupRecyclerviewAdapter.checkedCnt.observe(this, Observer { cnt ->
            menuItem.isEnabled = cnt > 0
        })

        viewModel.getGroupId().observe(this, Observer {
            it.getContentIfNotHandled()?.let { groupId ->
                val intent = Intent(this, ChattingRoomActivity::class.java)
                intent.putExtra("groupId", groupId)
                intent.putExtra("groupMembers", selectGroupRecyclerviewAdapter.groupMembers)
                startActivity(intent)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if (intent.getStringExtra("before") != null) {
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
                intent.putExtra("invited member", selectGroupRecyclerviewAdapter.groupMembers)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select_group
    }
}