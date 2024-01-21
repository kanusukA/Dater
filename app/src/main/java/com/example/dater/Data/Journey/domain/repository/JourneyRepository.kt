package com.example.dater.Data.Journey.domain.repository

import com.example.dater.Data.Journey.domain.model.Journey
import kotlinx.coroutines.flow.Flow

interface JourneyRepository {

    fun getJourney(): Flow<List<Journey>>

    fun getJourneyByAsc(): Flow<List<Journey>>

    fun getJourneyByDesc(): Flow<List<Journey>>

    suspend fun getJourneyById(id: Int): Journey?

    suspend fun insertJourney(journey: Journey)

    suspend fun deleteJourney(journey: Journey)

}