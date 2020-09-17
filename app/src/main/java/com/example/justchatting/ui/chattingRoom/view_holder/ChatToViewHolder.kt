package com.example.justchatting.ui.chattingRoom.view_holder

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.ChatMessageModel
import com.example.justchatting.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatToViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var chatMessageModel : ChatMessageModel? = null

    @SuppressLint("SimpleDateFormat")
    fun bind(chatMessageModel: ChatMessageModel) {
        this.chatMessageModel = chatMessageModel

        if(chatMessageModel.type == "image") {
            val profileImage =
                itemView.findViewById<CircleImageView>(R.id.chat_to_text_profile_image)
            val imageView = itemView.findViewById<ImageView>(R.id.chat_to_image_imageview)
            val timeStamp = itemView.findViewById<TextView>(R.id.chat_to_text_timestamp)

            if (chatMessageModel!!.profileImageUrl.isEmpty())
                profileImage.setImageResource(R.drawable.person)
            else
                Picasso.get().load(chatMessageModel.profileImageUrl).into(profileImage)

            Picasso.get().load(chatMessageModel.imageUri).into(imageView)
            timeStamp.text = chatMessageModel.timeStamp.toString()
        } else {
            val profileImage =
                itemView.findViewById<CircleImageView>(R.id.chat_to_text_profile_image)
            val textView = itemView.findViewById<TextView>(R.id.chat_to_text_textview)
            val timeStamp = itemView.findViewById<TextView>(R.id.chat_to_text_timestamp)

            if (chatMessageModel!!.profileImageUrl.isEmpty())
                profileImage.setImageResource(R.drawable.person)
            else
                Picasso.get().load(chatMessageModel.profileImageUrl).placeholder(R.drawable.person)
                    .into(profileImage)

            textView.text = chatMessageModel.text
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                timeStamp.text = simpleDateFormat.format(Date(chatMessageModel.timeStamp)).toString()
            }catch (e: Exception){}
        }
    }
}