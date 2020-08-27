package com.example.justchatting.ui.friend

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentFriendBinding
import com.example.justchatting.ui.friend.dialog.AddFriendFragment
import com.example.justchatting.ui.friend.dialog.TabDialogFragment

import com.squareup.picasso.Picasso
import com.example.justchatting.ui.login.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_friend.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FriendFragment : BaseFragment<FragmentFriendBinding>(){
    private val viewModel: FriendViewModel by viewModel()
    private lateinit var friendAdapter : FriendAdapter

    override fun getLayoutId(): Int = R.layout.fragment_friend

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setPermission()

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

//                val tabDialogFragment = TabDialogFragment()
//                tabDialogFragment.show(fragmentManager,"dialog")

            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun deleteAddFriendDialog()
    {
        val prev = childFragmentManager.findFragmentByTag("dialog")
        if(prev != null) {
            (prev as DialogFragment).dismiss()
        }
    }
    private fun isSuccessfulDialog(message: String, buttonName: String)
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

//    override fun messageFromTabDialog(selection : Int, input : String) {
//        when(selection) {
//            0->{
//                deleteAddFriendDialog()
//            }
//            1->{
//                viewModel.addFriendWithPhoneNumber(input)
//            }
//            2->{
//                val re = Regex("[^A-Za-z0-9 ]")
//                val input = re.replace(input,"")
//                viewModel.addFriendWithId(input)
//            }
//        }
//    }

    private fun setPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                requireContext() , Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            friendAdapter = FriendAdapter(viewModel.friendList.value!!)
            setHasOptionsMenu(true)
            friend_recyclerview.apply {
                adapter = friendAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
            }

            viewModel.friendList.observe(viewLifecycleOwner, Observer {
                friendAdapter.notifyDataSetChanged()
            })
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setPermission()
            } else {
                requireActivity().finish()
            }
        }
    }
}