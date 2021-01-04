package com.example.justchatting.ui.chatting

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.justchatting.ChattingRoom
import com.example.justchatting.R
import com.example.justchatting.ui.chattingRoom.ChattingRoomActivity
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingRecyclerViewAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mChattingList : ArrayList<ChattingRoom>? = null

    fun setChattingList(arrayList: ArrayList<ChattingRoom>){
        this.mChattingList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatting_item, parent,false)
        return ChattingViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(mChattingList == null)
            return 0
        return mChattingList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChattingViewHolder).bind(mChattingList!![position])
    }

    inner class ChattingViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.chatting_title)
        val contents = itemView.findViewById<TextView>(R.id.chatting_contents)
        val timeStamp  = itemView.findViewById<TextView>(R.id.chatting_timestamp)
        val constraintLayout = itemView.findViewById<ConstraintLayout>(R.id.chatting_constraint_layout)

        var chattingModel : ChattingRoom? = null

        @SuppressLint("SimpleDateFormat")
        fun bind(chattingRoom: ChattingRoom){
            this.chattingModel = chattingRoom
            this.contents.text = chattingRoom.lastMessage
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-ddHH:mm:ss")
                timeStamp.text = simpleDateFormat.format(Date(chattingRoom.timeStamp)).toString()
            }catch (e: Exception){}

            title.text = chattingRoom.groupName
            constraintLayout.setOnClickListener {
                val intent = Intent(itemView.context, ChattingRoomActivity::class.java)
                intent.putExtra("groupId", chattingRoom.groupId)
                itemView.context.startActivity(intent)
            }
        }
    }
}