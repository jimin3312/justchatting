package com.example.justchatting.ui.friend

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil

import androidx.databinding.Observable

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentFriendBinding

import com.squareup.picasso.Picasso
import com.example.justchatting.ui.login.RegisterActivity
import io.reactivex.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.fragment_friend.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FriendFragment : BaseFragment<FragmentFriendBinding>() {
    private val viewModel: FriendViewModel by viewModel()
    private val friendAdapter = FriendAdapter()
    private val disposable = CompositeDisposable()

    override fun getLayoutId(): Int = R.layout.fragment_friend
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFriendBinding =
            DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
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

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPermission()
    }

    private fun setPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                this.requireContext(), Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            initFriends()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initFriends()
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun initFriends() {
        
        viewModel.loadMyUser()
        viewModel.loadUsers()

        friend_recyclerview.adapter = friendAdapter
        disposable.add(viewModel.users.subscribe(friendAdapter::submitList))

        viewModel.getMyUser().observe(viewLifecycleOwner, Observer { myUser ->
            if (myUser != null) {
                friend_my_textview_username.text = myUser.username
                Picasso.get().load(myUser.profileImageUrl)
                    .placeholder(R.drawable.person)
                    .into(friend_my_imageview_profile_image)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}