package com.example.justchatting.ui.friend

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.R
import com.example.justchatting.User
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentFriendBinding
import com.example.justchatting.ui.friend.dialog.TabDialogFragment

import com.squareup.picasso.Picasso
import com.example.justchatting.ui.login.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_friend.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FriendFragment : BaseFragment<FragmentFriendBinding>() {
    private val viewModel: FriendViewModel by viewModel()
    private val friendAdapter = FriendAdapter()
    private val disposable = CompositeDisposable()
    private var myUserId = FirebaseAuth.getInstance().uid

    override fun getLayoutId(): Int = R.layout.fragment_friend

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        friend_recyclerview.adapter =friendAdapter
        setHasOptionsMenu(true)
        loadFriends()
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
                var tabDialogFragment = TabDialogFragment()
                var fragmentManager = requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)

                var prev = requireActivity().supportFragmentManager.findFragmentByTag("dialog")
                if(prev != null)
                    fragmentManager.remove(prev)
                tabDialogFragment.show(fragmentManager,"dialog")

            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("CheckResult")
    private fun loadFriends() {
        viewModel.getAnyUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewModel.loadMyUser(myUserId!!)
                viewModel.loadUsers()
                setObserver()
            },{
                viewModel.sync()
                viewModel.loadMyUser(myUserId!!)
                viewModel.loadUsers()
                setObserver()
            })
    }

    @SuppressLint("CheckResult")
    private fun setObserver()
    {
        disposable.add(viewModel.users.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ pagedList->
                friendAdapter.submitList(pagedList)
            },{})
        )
        disposable.add(viewModel.myUser.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ myUser ->
                if(myUser!= null) {
                    Log.d("FriendFragment myuser", myUser.username)
                    friend_my_textview_username.text = myUser.username
                    Picasso.get().load(myUser.profileImageUrl)
                        .placeholder(R.drawable.person)
                        .into(friend_my_imageview_profile_image)
                }
            },{}))
    }
    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}