package com.example.dater.ui.homePage

import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState


sealed class HomePageEvents{

    data class UpdateReminder(val reminder: Reminder): HomePageEvents()
    data class DeleteJourney(val journey: Journey,val listOfReminders: List<Reminder> = emptyList()): HomePageEvents()
    data class EditJourney(val journey: Journey): HomePageEvents()
    data class DeleteReminder(val reminder: Reminder): HomePageEvents()
    data class ChangeState(val homePageState: TopFilterBarState): HomePageEvents()

}
