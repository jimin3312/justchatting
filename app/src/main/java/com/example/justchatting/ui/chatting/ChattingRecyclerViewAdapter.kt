package com.example.justchatting.ui.chatting

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.data.DTO.ChattingRoom
import com.example.justchatting.databinding.ChattingItemBinding
import com.example.justchatting.ui.chattingRoom.ChattingRoomActivity
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mChattingList: ArrayList<ChattingRoom>? = null

    fun setChattingList(arrayList: ArrayList<ChattingRoom>) {
        this.mChattingList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChattingViewHolder(
            ChattingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        if (mChattingList == null)
            return 0
        return mChattingList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChattingViewHolder).bind(mChattingList!![position])
    }

    inner class ChattingViewHolder(binding : ChattingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.chattingTitle
        val contents = binding.chattingContents
        val timeStamp = binding.chattingTimestamp
        var chattingModel: ChattingRoom? = null

        init{
            binding.setOnClickListener {
                val intent = Intent(itemView.context, ChattingRoomActivity::class.java)
                intent.putExtra("groupId", chattingModel!!.groupId)
                itemView.context.startActivity(intent)
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun bind(chattingRoom: ChattingRoom) {
            this.chattingModel = chattingRoom
            this.contents.text = chattingRoom.lastMessage
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-ddHH:mm:ss")
                timeStamp.text = simpleDateFormat.format(Date(chattingRoom.timeStamp)).toString()
            } catch (e: Exception) {
            }
            title.text = chattingRoom.groupName
        }
    }
}