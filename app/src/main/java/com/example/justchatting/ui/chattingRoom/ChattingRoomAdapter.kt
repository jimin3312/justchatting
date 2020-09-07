package com.example.justchatting.ui.chattingRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.ChatMessage
import com.example.justchatting.R
import com.example.justchatting.ui.chattingRoom.view_holder.ChatFromImageViewHolder
import com.example.justchatting.ui.chattingRoom.view_holder.ChatFromTextViewHolder
import com.example.justchatting.ui.chattingRoom.view_holder.ChatToImageViewHolder
import com.example.justchatting.ui.chattingRoom.view_holder.ChatToTextViewHolder

class ChattingRoomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        val CHAT_FROM_TEXT = 0
        val CHAT_FROM_IMAGE = 1
        val CHAT_TO_TEXT = 2
        val CHAT_TO_IMAGE = 3
    }
    private var chatMessages : ArrayList<ChatMessage>? = null

    fun setChattingLog(chattingLog : ArrayList<ChatMessage>){
        this.chatMessages = chattingLog
    }

    override fun getItemViewType(position: Int): Int {
        return when (chatMessages!![position].type) {
            CHAT_FROM_TEXT -> {
                CHAT_FROM_TEXT
            }
            CHAT_FROM_IMAGE -> {
                CHAT_FROM_IMAGE
            }
            CHAT_TO_TEXT -> {
                CHAT_TO_TEXT
            }
            else ->
                CHAT_TO_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            CHAT_FROM_TEXT->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_from_text_item, parent,false)
                ChatFromTextViewHolder(view)
            }
            CHAT_FROM_IMAGE->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_from_image_item, parent,false)
                ChatFromImageViewHolder(view)
            }
            CHAT_TO_TEXT->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_to_text_item, parent,false)
                ChatToTextViewHolder(view)
            }
            else->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_to_image_item, parent,false)
                ChatToImageViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        if(chatMessages == null)
            return 0
        return chatMessages!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ChatFromTextViewHolder->{
                holder.bind(chatMessages!![position])
            }
            is ChatFromImageViewHolder->{
                holder.bind(chatMessages!![position])
            }
            is ChatToTextViewHolder->{
                holder.bind(chatMessages!![position])
            }
            is ChatToImageViewHolder->{
                holder.bind(chatMessages!![position])
            }
        }
    }
}