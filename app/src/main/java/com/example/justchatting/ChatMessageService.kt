package com.example.justchatting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.justchatting.di.PREF
import com.example.justchatting.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import kotlin.random.Random

class ChatMessageService() : FirebaseMessagingService() {

    val repository: AuthRepository by inject()
    companion object{
        val CHANNEL_ID = "1234"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("메세지", "From: ${message.from}")
        Log.d("메세지", "title: ${message.data["title"]}")
        Log.d("메세지", "body: ${message.data["body"]}")
        Log.d("메세지", "id: ${message.data["chatRoomId"]}")


        val sharedPreferences : SharedPreferences = getSharedPreferences(PREF, 0)
        if(sharedPreferences.getBoolean("notification", true) && message.data["chatRoomId"] != JustApp.roomId) {
            sendNotification(message.data["title"], message.data["body"])
        }

    }

    private fun sendNotification(title: String?, message: String?) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        createNotificationChannel()

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name ="channel"
            val descriptionText = "message"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun onNewToken(token: String) {
        Log.d("새 토큰", token)

        if(FirebaseAuth.getInstance().uid != null) {
            repository.updateToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {
                    // updating error handle
                })
        }
    }
}