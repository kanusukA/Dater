package com.example.dater.Data.Reminder.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dater.Data.Reminder.utils.ReminderType

@Entity(tableName = "reminders")
data class Reminder(
    val startDate: Long,
    val endDate: Long,
    val title: String,
    val description: String,
    val reminderType: ReminderType,
    val dateType: ReminderDateType = ReminderDateType.SelectedDate,
    val selectedDays: List<Int> = listOf(0,0,0,0,0,0,0),

    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

sealed class ReminderDateType{
    object EmptyDate: ReminderDateType()
    object SelectedDays: ReminderDateType()
    object SelectedDate: ReminderDateType()
}