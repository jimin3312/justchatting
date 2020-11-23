package com.example.justchatting.ui.chatting


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justchatting.R
import com.example.justchatting.base.BaseFragment
import com.example.justchatting.databinding.FragmentChattingBinding
import kotlinx.android.synthetic.main.fragment_chatting.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ChattingFragment : BaseFragment<FragmentChattingBinding>() {
    companion object{
        val TAG = "ChattingFragment"
    }
    private val viewModel: ChattingViewModel by viewModel()
    private lateinit var chattingRecyclerviewAdapter: ChattingRecyclerViewAdapter
    override fun getLayoutId(): Int = R.layout.fragment_chatting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        chattingRecyclerviewAdapter = ChattingRecyclerViewAdapter()

        chatting_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = chattingRecyclerviewAdapter
        }

        chatting_recyclerview.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))

        viewModel.setListener()

        viewModel.getChattingRooms().observe(viewLifecycleOwner, Observer {
            Log.d(TAG,"chatting Rooms : ${it}")
            chattingRecyclerviewAdapter.setChattingList(it)
            chattingRecyclerviewAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chatting_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.chatting_create_chat_room->{
                val intent = Intent(requireContext(), SelectGroupActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
