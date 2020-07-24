package com.example.justchatting.ui.chatting


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentChattingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ChattingFragment : BaseFragment<FragmentChattingBinding>() {

    val viewModel: ChattingViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_chatting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
