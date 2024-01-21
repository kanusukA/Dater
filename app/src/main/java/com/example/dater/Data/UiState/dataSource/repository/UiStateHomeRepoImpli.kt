package com.example.dater.Data.UiState.dataSource.repository

import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.UiState.domain.model.UiState
import com.example.dater.Data.UiState.domain.repository.UiStateHomeRepository
import com.example.dater.ui.homePage.JourneySortType
import kotlinx.coroutines.flow.StateFlow

class UiStateHomeRepoImpli(
    private val uiState: UiState
):UiStateHomeRepository {
    override fun getJourneySort(): StateFlow<JourneySortType> {
        return uiState.homePageJourneySortType
    }

    override fun getReminderSort(): StateFlow<ReminderType> {
        return uiState.homePageReminderSortType
    }

}