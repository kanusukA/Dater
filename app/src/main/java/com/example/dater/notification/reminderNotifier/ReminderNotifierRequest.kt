package com.example.dater.notification.reminderNotifier

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.dater.Data.utils.DateHandler
import com.example.dater.ToastMessage
import com.example.dater.notification.PeriodicNotificationWorker
import com.example.dater.notification.SingleNotificationWorker
import com.example.dater.notification.WeeklyNotificationWorker
import java.time.Duration

class ReminderNotifierRequest (
    private val context: Context,
    private val reminderId: Int
){
    fun createOneTimeReminderNotifierRequest(
        delay: Duration,
        title: String,
        text: String
    ) {
        val work = OneTimeWorkRequestBuilder<SingleNotificationWorker>()
            .setInputData(inputData = workDataOf("title" to title, "text" to text))
            .setInitialDelay(
                delay
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(reminderId.toString(), ExistingWorkPolicy.REPLACE, work)
    }

    fun createPeriodicReminderNotifierRequest(
        end: Long,
        startFrom: Long = 0L,
        title: String,
        text: String
    ) {


        val work = PeriodicWorkRequestBuilder<PeriodicNotificationWorker>(Duration.ofHours(24))
            .setInputData(inputData = workDataOf("title" to title, "text" to text, "end" to end))
            .setInitialDelay(
                if(startFrom == 0L){
                    Duration.ofMillis(getReminderPeriodicDelay())
                } else {
                    Duration.ofMillis(startFrom - DateHandler().getLong())
                }
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(reminderId.toString(),ExistingPeriodicWorkPolicy.UPDATE,work)

    }

    fun createWeeklyReminderNotifierRequest(
        weeks: List<Int>,
        title: String,
        text: String
    ) {
        val work = OneTimeWorkRequestBuilder<WeeklyNotificationWorker>()
            .setInputData(inputData = workDataOf("title" to title, "text" to text, "weeks" to weeks.toIntArray()))
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(reminderId.toString(),ExistingWorkPolicy.REPLACE,work)

    }

    private fun getReminderPeriodicDelay(): Long{
        val nextReminder = DateHandler()
        nextReminder.addTO(1)
        nextReminder.setTime(12,0,0)
        return nextReminder.getLong() - DateHandler().getLong()
    }

    fun cancelReminderNotifier() {
        WorkManager.getInstance(context).cancelUniqueWork(reminderId.toString())
    }

    fun getReminderNotifierStatusToast():String {

        val workInfo = WorkManager.getInstance(context).getWorkInfosForUniqueWork(reminderId.toString()).get()

        if(!workInfo.isNullOrEmpty()){
            val days =
                ((workInfo[0].nextScheduleTimeMillis - DateHandler().getLong()) / 86400000L).toInt()
            val hour =
                ((workInfo[0].nextScheduleTimeMillis - DateHandler().getLong()) / 3600000L).toInt()
            val min =
                ((workInfo[0].nextScheduleTimeMillis - DateHandler().getLong()) / 60000L).toInt()
            val sec =
                ((workInfo[0].nextScheduleTimeMillis - DateHandler().getLong()) / 1000L).toInt()

            if (days <= 0) {
                return if(days < 0){
                    ToastMessage(context,"Passed")
                    "Passed"
                } else if (hour == 0) {
                    if (min == 0) {
                        ToastMessage(context, "Sec Left - $sec")
                        "Sec Left - $sec"
                    } else {
                        ToastMessage(context, "Min Left - $min")
                        "Min Left - $min"
                    }
                } else {
                    ToastMessage(context, "Hours Left - $hour")
                    "Hours Left - $hour"
                }
            } else {
                ToastMessage(context, "Days Left - $days")
                return "Days Left - $days"
            }
        } else {
            ToastMessage(context,"Reminder Not Set")
            return "Reminder Not Set"
        }

    }

}











