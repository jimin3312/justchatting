package com.example.justchatting.ui.friend.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabDialogAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val mFragmentCollection  = ArrayList<Fragment>()
    private val mTitleCollection = ArrayList<String>()

    fun addItem(title : String, fragment: Fragment)
    {
        mFragmentCollection.add(fragment)
        mTitleCollection.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentCollection[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleCollection[position]
    }

    override fun getCount(): Int {
        return mFragmentCollection.size
    }
}