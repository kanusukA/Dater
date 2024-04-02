package com.example.dater.ui.components.JourneyBox

import com.example.dater.Data.Reminder.utils.ReminderType

sealed class JourneyBoxEvents {
    data class SelectReminderType(val type: ReminderType): JourneyBoxEvents()
    object DeleteJourney: JourneyBoxEvents()
    object Expand: JourneyBoxEvents()
    object EditJourney: JourneyBoxEvents()
    object AlertState: JourneyBoxEvents()

}
