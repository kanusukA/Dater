package com.example.dater.Data.Journey.dataSource.repository

import com.example.dater.Data.Journey.dataSource.JourneyDao
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import kotlinx.coroutines.flow.Flow

class JourneyRepositoryImpli(
    private val dao: JourneyDao
): JourneyRepository {
    override fun getJourney(): Flow<List<Journey>> {
        return dao.getJourneys()
    }

    override fun getJourneyByAsc(): Flow<List<Journey>> {
        return dao.getJourneyByAsc()
    }

    override fun getJourneyByDesc(): Flow<List<Journey>> {
        return dao.getJourneyByDesc()
    }


    override suspend fun getJourneyById(id: Int): Journey? {
        return dao.getJourneyByID(id)
    }

    override suspend fun insertJourney(journey: Journey) {
        return dao.insertJourney(journey)
    }

    override suspend fun deleteJourney(journey: Journey) {
        return dao.deleteJourney(journey)
    }

}