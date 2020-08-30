package com.example.justchatting.ui.friend

import android.content.DialogInterface
import android.os.Bundle
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

        viewModel.getMyUsers().observe(viewLifecycleOwner, Observer {
            friend_my_textview_username.text = it.username
            if (it.profileImageUrl!!.isEmpty())
                friend_my_imageview_profile_image.setImageResource(R.drawable.person)
            else
                Picasso.get().load(it!!.profileImageUrl).into(friend_my_imageview_profile_image)
        })

        viewModel.getUsers().observe(viewLifecycleOwner, Observer {
            friendAdapter.setUsers(it)
        })
        viewModel.getAddFriend().observe(viewLifecycleOwner, Observer {friendAddSuccess->
            if(friendAddSuccess)
                resultDialog("친구추가 성공", "확인")
            else
                resultDialog("친구추가 실패", "확인")
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
}