package com.example.justchatting.ui.friend.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.example.justchatting.R
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.tabs.TabLayout

class TabDialogFragment: DialogFragment() , AddFriendFragment.OnAddFriendFragmentButtonListener {

    lateinit var tabLayout : TabLayout
    lateinit var inputIdFragment : AddFriendFragment
    lateinit var inputPhoneFragment : AddFriendFragment
    private var mTabDialogFragmentListener: OnTabDialogFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_friend_layout, null)
        tabLayout = view.findViewById(R.id.dialog_add_friend_tab)
        tabLayout.addTab(tabLayout.newTab().setText("연락처로 추가"))
        tabLayout.addTab(tabLayout.newTab().setText("ID로 추가"))

        inputIdFragment = AddFriendFragment("친구로 추가하기 위해 ID를 입력하세요.")
        inputPhoneFragment = AddFriendFragment("친구로 추가하기 위해 전화번호를 입력하세요.")

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position)
                {
                    0->{
                        childFragmentManager.beginTransaction().replace(R.id.dialog_fragment_container, inputIdFragment).commit()

                    }
                    1->{
                        childFragmentManager.beginTransaction().replace(R.id.dialog_fragment_container, inputPhoneFragment).commit()
                    }
                }
            }
        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is OnTabDialogFragmentListener)
            mTabDialogFragmentListener = context as OnTabDialogFragmentListener
        if(parentFragment is OnTabDialogFragmentListener)
        {
            mTabDialogFragmentListener = context as OnTabDialogFragmentListener
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        childFragmentManager.beginTransaction().replace(R.id.dialog_fragment_container, inputIdFragment).commit()
    }

    override fun onResume() {
        super.onResume()
        childFragmentManager.beginTransaction().replace(R.id.dialog_fragment_container, inputIdFragment)
        dialog!!.getWindow()!!.setLayout(1000, 700);
    }

    override fun onDetach() {
        super.onDetach()
        mTabDialogFragmentListener = null
    }
    interface OnTabDialogFragmentListener {
        fun messageFromTabDialog(isAdd : Boolean)
    }

    override fun messageFromAddFriendFragment(isAdd: Boolean) {
        Log.d("TabDialogFragment", "TabDialog add : $isAdd")
        if(mTabDialogFragmentListener == null)
            Log.d("TabDialogFragment", "TabDialoglistener null")
        mTabDialogFragmentListener?.messageFromTabDialog(isAdd)
    }

}

