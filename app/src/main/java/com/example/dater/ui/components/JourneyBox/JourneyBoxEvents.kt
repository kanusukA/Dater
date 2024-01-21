package com.example.dater.ui.components.JourneyBox

import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType

sealed class JourneyBoxEvents {
    data class SelectReminderType(val type: ReminderType): JourneyBoxEvents()
    object DeleteJourney: JourneyBoxEvents()
    object Expand: JourneyBoxEvents()
    object EditJourney: JourneyBoxEvents()
}

data class ReminderDotCount(
    val alert: Int,
    val event: Int,
    val birthday: Int
)