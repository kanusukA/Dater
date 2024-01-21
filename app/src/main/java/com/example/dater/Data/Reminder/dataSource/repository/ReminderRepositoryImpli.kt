package com.example.dater.Data.Reminder.dataSource.repository

import com.example.dater.Data.Reminder.dataSource.ReminderDao
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import com.example.dater.Data.Reminder.utils.ReminderType
import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpli(
    private val reminderDao: ReminderDao
): ReminderRepository {
    override fun getReminders(): Flow<List<Reminder>> {
        return reminderDao.getReminders()
    }

    override fun getRemindersByType(type: ReminderType): Flow<List<Reminder>> {
        return reminderDao.getRemindersByType(type)
    }


    override suspend fun getReminderById(id: Int): Reminder? {
        return reminderDao.getReminderById(id)
    }

    override fun getReminderList(list: List<Long>): Flow<List<Reminder>> {
        return reminderDao.getReminderList(list)
    }

    override suspend fun updateReminder(reminder: Reminder) {
        return reminderDao.updateReminder(reminder)
    }

    override suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(reminder)
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        return reminderDao.deleteReminder(reminder)
    }
}