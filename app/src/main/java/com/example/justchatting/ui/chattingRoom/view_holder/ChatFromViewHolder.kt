package com.example.justchatting.ui.chattingRoom.view_holder

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.Message
import com.example.justchatting.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatFromViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
    var message : Message? = null

    @SuppressLint("SimpleDateFormat")
    fun bind(message: Message) {
        this.message=message

        if(message.type == "image") {
            val profileImage =
                itemView.findViewById<CircleImageView>(R.id.chat_from_image_profile_image)
            val imageView = itemView.findViewById<ImageView>(R.id.chat_to_image_imageview)
            val timeStamp = itemView.findViewById<TextView>(R.id.chat_from_text_timestamp)
            val name = itemView.findViewById<TextView>(R.id.chat_from_image_name)
            if (message.profileImageUrl.isEmpty())
                profileImage.setImageResource(R.drawable.person)
            else
                Picasso.get().load(message.profileImageUrl).into(profileImage)

            Picasso.get().load(message.imageUri).into(imageView)
            name.text = message.name

            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                timeStamp.text = simpleDateFormat.format(Date(message.timeStamp)).toString()
            }catch (e: Exception){}

        } else {
            val profileImage = itemView.findViewById<CircleImageView>(R.id.chat_from_text_profile_image)
            val textView = itemView.findViewById<TextView>(R.id.chat_from_text_textview)
            val timeStamp = itemView.findViewById<TextView>(R.id.chat_from_text_timestamp)
            val name = itemView.findViewById<TextView>(R.id.chat_from_text_name)

            if(message.profileImageUrl.isEmpty())
                profileImage.setImageResource(R.drawable.person)
            else
                Picasso.get().load(message.profileImageUrl).placeholder(R.drawable.person).into(profileImage)

            textView.text = message.text
            name.text = message.name

            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                timeStamp.text = simpleDateFormat.format(Date(message.timeStamp)).toString()
            }catch (e: Exception){}
        }
    }
}