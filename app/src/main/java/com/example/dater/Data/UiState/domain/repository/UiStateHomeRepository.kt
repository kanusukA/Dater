package com.example.dater.Data.UiState.domain.repository

import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.ui.homePage.JourneySortType
import kotlinx.coroutines.flow.StateFlow

interface UiStateHomeRepository {

    fun getJourneySort(): StateFlow<JourneySortType>

    fun getReminderSort(): StateFlow<ReminderType>
}