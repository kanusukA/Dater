package com.example.dater.Data.Journey.domain.repository

import android.content.Context
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.model.JourneyWidgetSelection
import kotlinx.coroutines.flow.Flow

interface JourneyRepository {

    fun getJourney(): Flow<List<Journey>>

    fun getJourneyByAsc(): Flow<List<Journey>>

    fun getJourneyByDesc(): Flow<List<Journey>>

    fun getJourneySelection(): Flow<JourneyWidgetSelection>

    fun startAlarm(journey: Journey,journeyId: Long)

    fun cancelAlarm(journeyId: Long)

    suspend fun setJourneySelection(journeyWidgetSelection: JourneyWidgetSelection)

    suspend fun getJourneyById(id: Int): Journey?

    suspend fun insertJourney(journey: Journey)

    suspend fun updateJourney(journey: Journey)

    suspend fun deleteJourney(journey: Journey)

}