package com.example.dater.Data.Reminder.domain.repository

import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getReminders(): Flow<List<Reminder>>

    fun getRemindersByType(type: ReminderType): Flow<List<Reminder>>

    suspend fun getReminderById(id: Int): Reminder?

    fun getReminderList(list: List<Long>): Flow<List<Reminder>>

    suspend fun updateReminder(reminder: Reminder)

    suspend fun insertReminder(reminder: Reminder): Long

    suspend fun deleteReminder(reminder: Reminder)
}