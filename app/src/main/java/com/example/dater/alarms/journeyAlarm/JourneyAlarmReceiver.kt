package com.example.dater.alarms.journeyAlarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dater.alarms.notification.DaterNotification

class JourneyAlarmReceiver(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        
        val title = intent?.getStringExtra("Title") ?: "No title"
        val id = intent?.getLongExtra("id",-1) ?: -1

        context?.let { cxt ->

            val daterNotification = DaterNotification(cxt)
            daterNotification.journeyNotify(title,id.toInt())

        }
    }
}