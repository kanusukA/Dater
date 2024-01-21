package com.example.dater.notification


import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.dater.R
import kotlin.random.Random

class ReminderNotifications(
    private val context: Context
){
    private val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java)

    fun showBasicNotification(title: String, text: String){
        val builder = NotificationCompat.Builder(context,context.getString(R.string.reminder_channel_id))
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.dater_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            builder
        )

    }

}