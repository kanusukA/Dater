package com.example.dater.alarms.reminderAlarm


import com.example.dater.Data.Reminder.utils.ReminderType

interface ReminderScheduler {
    fun schedule(alarmItem: ReminderAlarmItem)

    fun cancel(reminderType: ReminderType, reminderId: Long)
}