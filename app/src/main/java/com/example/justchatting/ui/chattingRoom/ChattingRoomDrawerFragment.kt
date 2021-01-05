package com.example.justchatting.ui.chattingRoom

import android.os.Bundle
import android.view.View
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentChattingRoomDrawerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChattingRoomDrawerFragment: BaseFragment<FragmentChattingRoomDrawerBinding>() {

    val viewModel: ChattingRoomViewModel by viewModel()

    override fun getLayoutId() = R.layout.fragment_chatting_room_drawer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


    }

}