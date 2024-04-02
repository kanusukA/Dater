package com.example.dater.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.utils.DateHandler
import com.example.dater.alarms.reminderAlarm.ReminderSchedulerImpl
import com.example.dater.alarms.notification.DaterNotification

class AlarmReceiverRepeating(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {


        val text = intent?.getStringExtra("Text") ?: "No Text"
        val endDate = intent?.getLongExtra("EndDate",-1) ?: -1
        val id = intent?.getLongExtra("id",-1) ?: -1
        val title = ("Day: ${DateHandler().getDaysLeft(endDate)} " + intent?.getStringExtra("Title")) ?: "No Title"

        context?.let { cxt ->

            if(endDate < DateHandler().getLong()){
                val scheduler = ReminderSchedulerImpl(cxt)
                scheduler.cancel(ReminderType.Event,id)
            }

            val daterNotification = DaterNotification(cxt)
            daterNotification.reminderNotify(title,text,id.toInt())
        }

    }

}