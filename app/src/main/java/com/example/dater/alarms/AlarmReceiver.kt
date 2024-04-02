package com.example.dater.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dater.alarms.notification.DaterNotification

class AlarmReceiver(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val title = intent?.getStringExtra("Title") ?: "No Title"
        val text = intent?.getStringExtra("Text") ?: "No Text"
        val id = intent?.getLongExtra("id",-1) ?: -1

        context?.let {cxt ->

            val daterNotification = DaterNotification(cxt)

            daterNotification.reminderNotify(title,text,id.toInt())
        }

    }
}