package com.example.dater.Data.Reminder.dataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dater.Data.Journey.dataSource.JourneyDatabase
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.model.ReminderTypeConvertor

@Database(
    entities = [Reminder::class],
    version = 1
)
@TypeConverters(ReminderTypeConvertor::class)
abstract class ReminderDataBase:RoomDatabase(){

    abstract fun reminderDao(): ReminderDao

    companion object{
        const val DATABASE_NAME = "reminder-db"

        @Volatile
        private var INSTANCE: ReminderDataBase? = null

        fun getDatabase(
            context: Context
        ):ReminderDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDataBase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}