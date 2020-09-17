package com.example.justchatting.ui.chattingRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.ChatMessageModel
import com.example.justchatting.R
import com.example.justchatting.ui.chattingRoom.view_holder.ChatFromViewHolder
import com.example.justchatting.ui.chattingRoom.view_holder.ChatToViewHolder
import com.google.firebase.auth.FirebaseAuth

class ChattingRoomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        val CHAT_FROM  = 0
        val CHAT_TO = 1
    }
    private var chatMessageModels : ArrayList<ChatMessageModel>? = null
    private var uid = FirebaseAuth.getInstance().uid
    fun setChattingLog(chattingLog : ArrayList<ChatMessageModel>){
        this.chatMessageModels = chattingLog
    }

    override fun getItemViewType(position: Int): Int {
        return if(chatMessageModels!![position].fromId == uid ){
            CHAT_FROM
        } else{
            CHAT_TO
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            CHAT_FROM->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_from_text_item, parent,false)
                ChatFromViewHolder(view)
            }
            else->{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_to_text_item, parent,false)
                ChatToViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        if(chatMessageModels == null)
            return 0
        return chatMessageModels!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ChatFromViewHolder->{
                holder.bind(chatMessageModels!![position])
            }
            is ChatToViewHolder->{
                holder.bind(chatMessageModels!![position])
            }
        }
    }
}