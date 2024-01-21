package com.example.dater.Data.Reminder.dataSource

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders")
    fun getReminders(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE reminderType == :type")
    fun getRemindersByType(type: ReminderType): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id IN (:list)")
    fun getReminderList(list: List<Long>): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE id == :id")
    suspend fun getReminderById(id: Int): Reminder?

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Delete
    suspend fun deleteReminder(reminder: Reminder)
}