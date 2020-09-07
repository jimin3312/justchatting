package com.example.justchatting.ui.chattingRoom.view_holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.ChatMessage
import com.example.justchatting.ChatMessageText
import com.example.justchatting.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatFromTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var chatMessage : ChatMessage? = null

    fun bind(chatMessage: ChatMessage) {
        this.chatMessage=chatMessage

        val chat = (chatMessage as ChatMessageText)
        val profileImage = itemView.findViewById<CircleImageView>(R.id.chat_from_text_profile_image)
        val textView = itemView.findViewById<TextView>(R.id.chat_from_text_textview)
        val timeStamp = itemView.findViewById<TextView>(R.id.chat_from_text_timestamp)

        if(chat.profileImageUrl.isEmpty())
            profileImage.setImageResource(R.drawable.person)
        else
            Picasso.get().load(chat.profileImageUrl).placeholder(R.drawable.person).into(profileImage)

        textView.text = chat.text
        timeStamp.text = chat.timeStamp.toString()
    }
}