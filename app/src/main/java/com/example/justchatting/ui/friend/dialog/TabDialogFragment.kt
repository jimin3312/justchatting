package com.example.justchatting.ui.friend.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.example.justchatting.R
import com.google.android.material.tabs.TabLayout

class TabDialogFragment : DialogFragment() {
    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager
    lateinit var tabDialogAdapter: TabDialogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_friend_layout, null)
        tabLayout = view.findViewById(R.id.dialog_add_friend_tab)
        viewPager = view.findViewById(R.id.dialog_viewPager)
        tabDialogAdapter = TabDialogAdapter(childFragmentManager)

        tabDialogAdapter.addItem("연락처로 추가", CustomFragment.createInstance("친구로 추가하기 위해 전화번호를 입력하세요"))
        tabDialogAdapter.addItem("ID로 추가", CustomFragment.createInstance("친구로 추가하기 위해 ID를 입력하세요."))
        viewPager.adapter = tabDialogAdapter
        tabLayout.setupWithViewPager(viewPager)


        return view
    }

    override fun onResume() {
        super.onResume()
        dialog!!.getWindow()!!.setLayout(1000, 700);

    }

}