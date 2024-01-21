package com.example.dater.di

import android.app.Application
import com.example.dater.Data.Journey.dataSource.JourneyDatabase
import com.example.dater.Data.Journey.dataSource.repository.JourneyRepositoryImpli
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import com.example.dater.Data.Reminder.dataSource.ReminderDataBase
import com.example.dater.Data.Reminder.dataSource.repository.ReminderRepositoryImpli
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import com.example.dater.Data.UiState.dataSource.repository.UiStateRepoImpli
import com.example.dater.Data.UiState.domain.model.UiState
import com.example.dater.Data.UiState.domain.repository.UiStateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideJourneyDatabase(app: Application): JourneyDatabase {
        return JourneyDatabase.getDataBase(app)
    }

    @Provides
    @Singleton
    fun provideReminderDataBase(app: Application): ReminderDataBase {
        return ReminderDataBase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun provideJourneyRepository(db: JourneyDatabase): JourneyRepository {
        return JourneyRepositoryImpli(db.journeyDao())
    }

    @Provides
    @Singleton
    fun provideReminderRepository(db: ReminderDataBase): ReminderRepository {
        return ReminderRepositoryImpli(db.reminderDao())
    }

}