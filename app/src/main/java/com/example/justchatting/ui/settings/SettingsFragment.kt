package com.example.justchatting.ui.settings


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    val viewModel: SettingsViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        (activity as AppCompatActivity).setSupportActionBar(binding.settingsToolbar)
    }
}
