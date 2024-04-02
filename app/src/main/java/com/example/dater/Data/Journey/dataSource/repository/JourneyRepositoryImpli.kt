package com.example.dater.Data.Journey.dataSource.repository

import android.app.Application
import android.content.Context
import com.example.dater.Data.Journey.dataSource.JourneyDao
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.model.JourneyWidgetSelection
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import com.example.dater.alarms.journeyAlarm.JourneyAlarmItem
import com.example.dater.alarms.journeyAlarm.JourneyAlarmSchedulerImpl
import kotlinx.coroutines.flow.Flow

class JourneyRepositoryImpli(
    private val dao: JourneyDao,
    private val context: Context
): JourneyRepository {

    private val journeyAlarmScheduler = JourneyAlarmSchedulerImpl(context)

    override fun getJourney(): Flow<List<Journey>> {
        return dao.getJourneys()
    }

    override fun getJourneyByAsc(): Flow<List<Journey>> {
        return dao.getJourneyByAsc()
    }

    override fun getJourneyByDesc(): Flow<List<Journey>> {
        return dao.getJourneyByDesc()
    }

    override fun getJourneySelection(): Flow<JourneyWidgetSelection> {
        return dao.getJourneySelection(0)
    }

    override fun startAlarm(journey: Journey, journeyId: Long) {
        journeyAlarmScheduler.schedule(JourneyAlarmItem(journey, journeyId))
    }

    override fun cancelAlarm(journeyId: Long) {
        journeyAlarmScheduler.cancel(journeyId)
    }

    override suspend fun setJourneySelection(journeyWidgetSelection: JourneyWidgetSelection) {
        return dao.setJourneySelection(journeyWidgetSelection)
    }

    override suspend fun getJourneyById(id: Int): Journey? {
        return dao.getJourneyByID(id)
    }

    override suspend fun insertJourney(journey: Journey) {
        val id = dao.insertJourney(journey)
        if(journey.notification){
            startAlarm(journey,id)
        }else{
            cancelAlarm(id)
        }
    }

    override suspend fun updateJourney(journey: Journey) {
        dao.updateJourney(journey)
        if(journey.notification){
            startAlarm(journey,journey.id.toLong())
        }else{
            cancelAlarm(journey.id.toLong())
        }
    }

    override suspend fun deleteJourney(journey: Journey) {
        cancelAlarm(journey.id.toLong())
        dao.deleteJourney(journey)
    }


}