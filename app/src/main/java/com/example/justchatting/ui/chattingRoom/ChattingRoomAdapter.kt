package com.example.justchatting.ui.chattingRoom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.Message
import com.example.justchatting.R
import com.example.justchatting.ui.chattingRoom.view_holder.ChatFromViewHolder
import com.example.justchatting.ui.chattingRoom.view_holder.ChatToViewHolder
import com.google.firebase.auth.FirebaseAuth

class ChattingRoomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        val CHAT_FROM  = 0
        val CHAT_TO = 1
    }
    private var messages : ArrayList<Message>? = null
    private var uid = FirebaseAuth.getInstance().uid
    fun setChattingLog(chattingLog : ArrayList<Message>){
        this.messages = chattingLog
    }

    override fun getItemViewType(position: Int): Int {
        return if(messages!![position].fromId == uid ){
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
        if(messages == null)
            return 0
        return messages!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ChatFromViewHolder->{
                holder.bind(messages!![position])
            }
            is ChatToViewHolder->{
                holder.bind(messages!![position])
            }
        }
    }
}