package com.example.justchatting.ui.friend

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentFriendBinding
import com.example.justchatting.ui.login.RegisterActivity
import kotlinx.android.synthetic.main.fragment_friend.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class FriendFragment : BaseFragment<FragmentFriendBinding>() {
    private val model: FriendViewModel by viewModel()

    override fun getLayoutId(): Int = R.layout.fragment_friend
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentFriendBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.viewModel = model
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friend_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.friend_sync_button->{
                model.sync()
            }
            R.id.friend_add_friend_button->{

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val friendRecyclerViewAdapter = FriendAdapter(model.getUsers())
        friend_recyclerview.adapter = friendRecyclerViewAdapter
        friend_recyclerview.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        model.getUsers().observe(viewLifecycleOwner, Observer {
//            val uId = FirebaseAuth.getInstance().uid
//            val myUserRef = FirebaseDatabase.getInstance().getReference("/users/$uId")
//            myUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                }
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val myUser = snapshot.getValue(User::class.java)?:return
//                    it.add(0,myUser)
//                }
//            })
            friendRecyclerViewAdapter.notifyDataSetChanged()
        })

        syncContacts()
    }
    private fun syncContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                this.requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else{
            model.sync()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RegisterActivity.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                model.sync()
            } else
            {
                requireActivity().finish()
            }
        }
    }

}
