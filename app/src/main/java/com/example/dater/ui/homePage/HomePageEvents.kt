package com.example.dater.ui.homePage

import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.utils.JourneyWidgetType
import com.example.dater.Data.Reminder.domain.model.Reminder



sealed class HomePageEvents {

    data class UpdateReminder(val reminder: Reminder) : HomePageEvents()
    data class DeleteJourney(
        val journey: Journey,
        val listOfReminders: List<Reminder> = emptyList()
    ) : HomePageEvents()

    data class EditJourney(val journey: Journey) : HomePageEvents()

    data class ChangeJourneyWidgetType(
        val journeyWidgetType: JourneyWidgetType,
        val journey: Journey,
    ) : HomePageEvents()

    data class ForceJourneyWidgetToPrimary(val journey: Journey):HomePageEvents()

    data class DeleteReminder(val reminder: Reminder) : HomePageEvents()

}
