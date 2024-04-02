package com.example.dater.Data.Reminder.dataSource.repository

import android.content.Context
import com.example.dater.Data.Reminder.dataSource.ReminderDao
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.alarms.reminderAlarm.ReminderAlarmItem
import com.example.dater.alarms.reminderAlarm.ReminderScheduler
import com.example.dater.alarms.reminderAlarm.ReminderSchedulerImpl

import kotlinx.coroutines.flow.Flow


class ReminderRepositoryImpli(
    private val reminderDao: ReminderDao,
    private val context: Context
): ReminderRepository {

    private val reminderAlarmScheduler = ReminderSchedulerImpl(context)
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

    override suspend fun insertReminder(reminder: Reminder,context: Context): Long {
        val id = reminderDao.insertReminder(reminder)
        startAlarm(reminder,id)
        return id
    }

    override suspend fun deleteReminder(reminder: Reminder,context: Context) {
        cancelAlarm(reminder,reminder.id.toLong())
        reminderDao.deleteReminder(reminder)
    }

    fun startAlarm(reminder: Reminder,reminderId: Long){
        reminderAlarmScheduler.schedule(ReminderAlarmItem(reminder, reminderId))
    }

    fun cancelAlarm(reminder: Reminder,reminderId: Long){
        reminderAlarmScheduler.cancel(reminder.reminderType, reminderId)
    }
}