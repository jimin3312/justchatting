package com.example.justchatting.ui.friend

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentFriendBinding
import com.example.justchatting.ui.chatting.SelectGroupActivity
import com.example.justchatting.ui.chattingRoom.ChattingRoomActivity
import com.example.justchatting.ui.friend.dialog.TabDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FriendFragment : BaseFragment<FragmentFriendBinding>() {
    private val viewModel: FriendViewModel by viewModel()
    private lateinit var friendAdapter : FriendAdapter

    override fun getLayoutId(): Int = R.layout.fragment_friend

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        friendAdapter = FriendAdapter()

        friend_recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendAdapter
        }

        viewModel.sync()
        viewModel.setListener()

        viewModel.getUsers().observe(viewLifecycleOwner, Observer {
            Log.d("Friend", it.toString())
            friendAdapter.setUsers(it)
            friendAdapter.notifyDataSetChanged()
        })

        viewModel.getAddFriend().observe(viewLifecycleOwner, Observer {friendAddSuccess->
            when (friendAddSuccess) {
                1 -> {
                    resultDialog("친구추가 성공", "확인")
                    viewModel.getAddFriend().postValue(0)
                    deleteAddFriendDialog()
                }
                -1 -> {
                    resultDialog("친구추가 실패", "확인")
                    viewModel.getAddFriend().postValue(0)
                }
                -2 -> {
                    resultDialog("이미 있는 친구입니다.", "확인")
                    viewModel.getAddFriend().postValue(0)
                }
            }

        })

        friendAdapter.itemClick.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                viewModel.loadGroupId(friendAdapter.groupMembers)
            }
        })

        viewModel.getGroupId().observe(viewLifecycleOwner, Observer {groupId->
            if(groupId != "-1") {
                val intent = Intent(requireContext(), ChattingRoomActivity::class.java)
                intent.putExtra("groupId", groupId)
                intent.putExtra("groupMembers", friendAdapter.groupMembers)
                startActivity(intent)
                friendAdapter.itemClick.postValue(false)
                viewModel.getGroupId().postValue("-1")
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friend_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.friend_sync_button -> {
                viewModel.sync()
            }
            R.id.friend_add_friend_button -> {
                var fragmentManager = childFragmentManager
                fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .commit()

                val tabDialogFragment = TabDialogFragment()
                tabDialogFragment.show(fragmentManager,"dialog")

            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun resultDialog(message: String, buttonName: String)
    {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonName,DialogInterface.OnClickListener{ dialog, which ->
            dialog.dismiss()
        })
        alertDialog.show()
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val layoutParams = positiveButton.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 1.0f
        layoutParams.gravity = Gravity.CENTER
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).layoutParams = layoutParams
    }
    private fun deleteAddFriendDialog()
    {
        val fragment = childFragmentManager.findFragmentByTag("dialog")
        if(fragment != null) {
            (fragment as DialogFragment).dismiss()
        }
    }
}