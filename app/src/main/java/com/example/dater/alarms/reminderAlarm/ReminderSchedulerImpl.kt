package com.example.dater.alarms.reminderAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.dater.Data.Reminder.domain.model.ReminderDateType
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.utils.DateHandler
import com.example.dater.alarms.AlarmReceiver
import com.example.dater.alarms.AlarmReceiverRepeating

class ReminderSchedulerImpl(
    private val context: Context
) : ReminderScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    override fun schedule(alarmItem: ReminderAlarmItem) {

        when (alarmItem.reminder.reminderType) {

            ReminderType.Alert -> {

                when (alarmItem.reminder.dateType) {

                    ReminderDateType.EmptyDate -> {
                        TODO()
                    }

                    ReminderDateType.SelectedDate -> {
                        if (alarmItem.reminder.startDate > alarmItem.reminder.endDate) {

                            reminderSingleDay(
                                alarmItem.reminder.title,
                                alarmItem.reminder.description,
                                alarmItem.reminder.startDate,
                                alarmItem.reminderId
                            )

                        } else {

                            reminderSingleDay(
                                alarmItem.reminder.title,
                                alarmItem.reminder.description,
                                alarmItem.reminder.startDate,
                                alarmItem.reminderId
                            )

                            reminderSingleDay(
                                alarmItem.reminder.title,
                                alarmItem.reminder.description,
                                alarmItem.reminder.endDate,
                                alarmItem.reminderId
                            )
                        }
                    }

                    ReminderDateType.SelectedDays -> {
                        TODO()
                    }
                }
            }

            ReminderType.All -> {
                TODO()
            }

            ReminderType.Birthday -> {
                reminderSingleDay(
                    alarmItem.reminder.title,
                    alarmItem.reminder.description,
                    alarmItem.reminder.startDate,
                    alarmItem.reminderId
                )
            }

            ReminderType.Event -> {
                when (alarmItem.reminder.dateType) {

                    ReminderDateType.EmptyDate -> {

                        reminderTimeWindowAlarm(
                            alarmItem.reminder.title,
                            alarmItem.reminder.description,
                            DateHandler().apply {
                                addTO(1)
                                setTime(12, 0, 0)
                            }.getLong(),
                            alarmItem.reminder.endDate,
                            alarmItem.reminderId
                        )
                    }

                    ReminderDateType.SelectedDate -> {

                        reminderTimeWindowAlarm(
                            alarmItem.reminder.title,
                            alarmItem.reminder.description,
                            alarmItem.reminder.startDate,
                            alarmItem.reminder.endDate,
                            alarmItem.reminderId
                        )

                    }

                    ReminderDateType.SelectedDays -> {
                        TODO()
                    }
                }
            }
        }

    }

    override fun cancel(reminderType: ReminderType, reminderId: Long) {
        when (reminderType) {

            ReminderType.Event -> {
                alarmManager.cancel(
                    PendingIntent.getBroadcast(
                        context,
                        reminderId.toInt(),
                        Intent(context, AlarmReceiverRepeating::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }

            else -> {
                alarmManager.cancel(
                    PendingIntent.getBroadcast(
                        context,
                        reminderId.toInt(),
                        Intent(context, AlarmReceiver::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }
        }
    }


    private fun reminderTimeWindowAlarm(
        title: String,
        text: String,
        startDate: Long,
        endDate: Long,
        id: Long
    ) {

        val intent = Intent(context, AlarmReceiverRepeating::class.java).apply {
            putExtra("Title", title)
            putExtra("Text", text)
            putExtra("EndDate", endDate)
            putExtra("id", id)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            startDate,
            AlarmManager.INTERVAL_DAY,
            PendingIntent.getBroadcast(
                context,
                id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private fun reminderSingleDay(title: String, text: String, startDate: Long, id: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("Title", title)
            putExtra("Text", text)
            putExtra("id", id)
        }

        val time = DateHandler(startDate).apply { setTime(12, 0, 0) }

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            time.getLong(),
            PendingIntent.getBroadcast(
                context,
                id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        println("Alarm Set Repeating - $title time - ${DateHandler(time.getLong()).getFullText()}")
    }

    private fun reminderWeeklyDay() {
        //TODO
    }
    

}