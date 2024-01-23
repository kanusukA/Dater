package com.example.dater.notification

import android.content.Context
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dater.Data.utils.DateHandler
import com.example.dater.Data.utils.getNextReminderWeekDay



class SingleNotificationWorker(
    private val context: Context,
    private val params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        ReminderNotifications(context).showBasicNotification(
            params.inputData.getString("title") ?: "null",
            params.inputData.getString("text") ?: "null",
        )

        return Result.success()
    }

}

class WeeklyNotificationWorker(
    private val context: Context,
    private val params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {

        val weeks = params.inputData.getIntArray("weeks") ?: intArrayOf(7)

        getNextReminderWeekDay(weeks.asList())
        /*TODO CREATE WEEKLY REMINDER*/
//        ReminderNotifications(context).showBasicNotification(
//            params.inputData.getString("title") ?: "null",
//            params.inputData.getString("text") ?: "null",
//        )

        return Result.success()
    }

}

class PeriodicNotificationWorker(
    private val context: Context,
    private val params: WorkerParameters
) : Worker(context, params) {
    override fun doWork(): Result {

        return if(DateHandler().getLong() >= params.inputData.getLong("end", 0L)){

            WorkManager.getInstance(context).cancelWorkById(this.id)
            Result.success()

        }else{
            val days = DateHandler().getDaysLeft(params.inputData.getLong("end",0L))
            val title = "Day - $days" + params.inputData.getString("title")
            ReminderNotifications(context).showBasicNotification(
                title,
                params.inputData.getString("text") ?: "null"
            )
            Result.success()
        }
    }

}