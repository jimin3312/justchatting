package com.example.justchatting.ui.chatting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.R
import com.example.justchatting.databinding.ActivitySelectGroupBinding
import kotlinx.android.synthetic.main.activity_select_group.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectGroupActivity : AppCompatActivity() {
    lateinit var binding : ActivitySelectGroupBinding
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
    }
}