package com.example.dater.alarms.journeyAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.dater.Data.utils.DateHandler

class JourneyAlarmSchedulerImpl(
    private val context: Context
):JourneyAlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    override fun schedule(journeyAlarmItem: JourneyAlarmItem) {
        val intent = Intent(context,JourneyAlarmReceiver::class.java).apply {
            putExtra("Title",journeyAlarmItem.journey.title)
            putExtra("id",journeyAlarmItem.journeyId)
        }

        val time = DateHandler(journeyAlarmItem.journey.endDate).apply { setTime(12,0,0) }

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            time.getLong(),
            PendingIntent.getBroadcast(
                context,
                journeyAlarmItem.journeyId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

    }

    override fun cancel(journeyId: Long) {
        val intent = Intent(context,JourneyAlarmReceiver::class.java)

        alarmManager.cancel(PendingIntent.getBroadcast(
            context,
            journeyId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}