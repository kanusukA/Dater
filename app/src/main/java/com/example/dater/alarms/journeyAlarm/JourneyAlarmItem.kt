package com.example.dater.alarms.journeyAlarm

import com.example.dater.Data.Journey.domain.model.Journey

data class JourneyAlarmItem(
    val journey: Journey,
    val journeyId: Long
    )
