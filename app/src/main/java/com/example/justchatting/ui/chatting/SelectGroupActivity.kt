package com.example.justchatting.ui.chatting

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
import com.example.justchatting.databinding.ActivitySelectGroupBinding
import com.example.justchatting.ui.chattingRoom.ChattingRoomActivity
import kotlinx.android.synthetic.main.activity_select_group.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectGroupActivity : AppCompatActivity() {
    lateinit var binding : ActivitySelectGroupBinding
    lateinit var menuItem : MenuItem
    val viewModel: SelectGroupViewModel by viewModel()
    lateinit var selectGroupRecyclerviewAdapter: SelectGroupRecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_group)

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

        selectGroupRecyclerviewAdapter.checkedCnt.observe(this, Observer { cnt->
            Log.d("SelectGroup", cnt.toString())
            menuItem.isEnabled = cnt>0
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.select_menu, menu)
        menuItem = menu!!.findItem(R.id.select_confirm)
        menuItem.isEnabled = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.select_confirm->{
                val intent = Intent(this, ChattingRoomActivity::class.java)
                intent.putStringArrayListExtra("groupMembersId",selectGroupRecyclerviewAdapter.checkedArrayList)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}