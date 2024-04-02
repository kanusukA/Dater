package com.example.dater.Data.Journey.dataSource

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.model.JourneyWidgetSelection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


@Dao
interface JourneyDao {

    @Query("SELECT * FROM journeys")
    fun getJourneys(): Flow<List<Journey>>

    @Query("SELECT * FROM journeys ORDER BY endDate ASC")
    fun getJourneyByAsc(): Flow<List<Journey>>

    @Query("SELECT * FROM journeys ORDER BY endDate DESC")
    fun getJourneyByDesc(): Flow<List<Journey>>

    @WorkerThread
    @Query("SELECT * FROM journeySelection WHERE id = :id")
    fun getJourneySelection(id: Int): Flow<JourneyWidgetSelection>
    @WorkerThread
    @Update
    suspend fun setJourneySelection(journeyWidgetSelection: JourneyWidgetSelection)

    @Query("SELECT * FROM journeys WHERE id = :id")
    suspend fun getJourneyByID(id: Int): Journey?

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJourney(journey: Journey): Long

    @WorkerThread
    @Update
    suspend fun updateJourney(journey: Journey)

    @Delete
    suspend fun deleteJourney(journey: Journey)
}