package com.example.dater.Data.Journey.dataSource

import android.content.Context
import androidx.compose.ui.layout.ContentScale
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.model.JourneyTypeConvertor

@Database(
    entities = [Journey::class],
    version = 1
)

@TypeConverters(JourneyTypeConvertor::class)
abstract class JourneyDatabase: RoomDatabase() {

    abstract fun journeyDao(): JourneyDao

    // creates DataBase is not existing
    companion object{
        const val DATABASE_NAME = "journey-db"

        @Volatile
        private var INSTANCE: JourneyDatabase?= null

        fun getDataBase(
            context: Context
        ): JourneyDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JourneyDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}