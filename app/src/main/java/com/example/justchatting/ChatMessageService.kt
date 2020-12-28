package com.example.justchatting

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ChatMessageService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

    }

    override fun onNewToken(token: String) {
        Log.d("새 토큰", token)
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("/users/$uid").child("token").setValue(token)
        }
    }
}