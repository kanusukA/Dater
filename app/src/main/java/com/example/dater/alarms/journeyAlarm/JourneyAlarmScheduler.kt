package com.example.dater.alarms.journeyAlarm

interface JourneyAlarmScheduler {
    fun schedule(journeyAlarmItem: JourneyAlarmItem)
    fun cancel(journeyId: Long)
}