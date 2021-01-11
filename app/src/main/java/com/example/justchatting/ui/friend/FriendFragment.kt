package com.example.justchatting.ui.friend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.Dialog
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentFriendBinding
import com.example.justchatting.ui.chattingRoom.ChattingRoomActivity
import kotlinx.android.synthetic.main.fragment_friend.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FriendFragment : BaseFragment<FragmentFriendBinding>() {
    private val viewModel: FriendViewModel by viewModel()
    private lateinit var friendAdapter: FriendAdapter

    override fun getLayoutId(): Int = R.layout.fragment_friend

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        friendAdapter = FriendAdapter()

        with(binding.friendRecyclerview){
            setHasFixedSize(true)
            adapter = friendAdapter
        }
        viewModel.syncWithContacts()
        viewModel.setFriendListChangeListener()

        viewModel.getUsers().observe(viewLifecycleOwner, Observer {
            friendAdapter.setUsers(it)
            friendAdapter.notifyDataSetChanged()
        })

        viewModel.isValidToAdd().observe(viewLifecycleOwner, Observer { friendAddSuccess ->

            friendAddSuccess.getContentIfNotHandled()?.let {
                when (friendAddSuccess.peekContent()) {
                    AddResult.SUCCESS -> {
                        Dialog.create(requireContext(), "친구추가 성공", "확인")
                        deleteAddFriendDialog()
                    }
                    AddResult.FAILED -> {
                        Dialog.create(requireContext(), "친구추가 실패", "확인")
                    }
                    AddResult.EXIST -> {
                        Dialog.create(requireContext(), "친구추가 성공", "확인")
                    }
                }
            }
        })

        friendAdapter.itemClick.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                viewModel.loadGroupId(friendAdapter.groupMembers)
            }
        })

        viewModel.getGroupId().observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {groupId ->
                val intent = Intent(requireContext(), ChattingRoomActivity::class.java)
                intent.putExtra("groupId", groupId)
                intent.putExtra("groupMembers", friendAdapter.groupMembers)
                startActivity(intent)
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
                viewModel.syncWithContacts()
            }
            R.id.friend_add_friend_button -> {
                var fragmentManager = childFragmentManager
                fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .commit()

                val tabDialogFragment =
                    TabDialogFragment()
                tabDialogFragment.show(fragmentManager, "dialog")

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAddFriendDialog() {
        val fragment = childFragmentManager.findFragmentByTag("dialog")
        if (fragment != null) {
            (fragment as DialogFragment).dismiss()
        }
    }

    enum class AddResult {
        SUCCESS,
        FAILED,
        EXIST
    }
}