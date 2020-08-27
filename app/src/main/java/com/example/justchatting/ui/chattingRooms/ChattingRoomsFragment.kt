package com.example.justchatting.ui.chattingRooms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentChattingBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ChattingRoomsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChattingRoomsFragment : BaseFragment<FragmentChattingBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatting_rooms, container, false)
    }

    override fun getLayoutId(): Int = R.layout.fragment_chatting_rooms

}