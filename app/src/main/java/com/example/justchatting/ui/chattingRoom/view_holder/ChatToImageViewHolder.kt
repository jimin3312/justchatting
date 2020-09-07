package com.example.justchatting.ui.chattingRoom.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.ChatMessage
import com.example.justchatting.ChatMessageImage
import com.example.justchatting.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatToImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var chatMessage : ChatMessage? = null

    fun bind(chatMessage: ChatMessage) {
        this.chatMessage = chatMessage

        val chat = (chatMessage as ChatMessageImage)
        val profileImage =
            itemView.findViewById<CircleImageView>(R.id.chat_to_text_profile_image)
        val imageView = itemView.findViewById<ImageView>(R.id.chat_to_image_imageview)
        val timeStamp = itemView.findViewById<TextView>(R.id.chat_to_text_timestamp)

        if (chat!!.profileImageUrl.isEmpty())
            profileImage.setImageResource(R.drawable.person)
        else
            Picasso.get().load(chat.profileImageUrl).into(profileImage)

        Picasso.get().load(chat.imageUri).into(imageView)
        timeStamp.text = chat.timeStamp.toString()

    }
}