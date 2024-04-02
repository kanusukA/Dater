package com.example.dater.alarms.reminderAlarm

import com.example.dater.Data.Reminder.domain.model.Reminder


data class ReminderAlarmItem(
    val reminder: Reminder,
    val reminderId: Long
)
