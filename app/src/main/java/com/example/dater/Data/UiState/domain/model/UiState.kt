package com.example.dater.Data.UiState.domain.model

import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.ui.addEditPage.AddEditEvents
import com.example.dater.ui.addEditPage.AddEditViewState
import com.example.dater.ui.addEditPage.DateType
import com.example.dater.ui.components.BottomNavBar.BottomNavBarEvents
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType
import com.example.dater.ui.homePage.JourneySortType
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@ViewModelScoped
class UiState(

) {
    // ViewModel States
    private val _bottomNavBarState: MutableStateFlow<BottomNavBarState> =
        MutableStateFlow(BottomNavBarState.HomePage)
    val bottomNavBarState: StateFlow<BottomNavBarState> = _bottomNavBarState

    private val _topFilterBarState: MutableStateFlow<TopFilterBarState> =
        MutableStateFlow(TopFilterBarState.JourneyState(TopFilterBarJourneyType.Ascending))
    val topFilterBarState: StateFlow<TopFilterBarState> = _topFilterBarState

    private val _homePageJourneySort: MutableStateFlow<JourneySortType> =
        MutableStateFlow(JourneySortType.ASC)
    val homePageJourneySortType: StateFlow<JourneySortType> = _homePageJourneySort

    private val _addEditViewState: MutableStateFlow<AddEditViewState> =
        MutableStateFlow(AddEditViewState.JOURNEY)
    val addEditViewState: StateFlow<AddEditViewState> = _addEditViewState

    private val _homePageReminderSortType: MutableStateFlow<ReminderType> =
        MutableStateFlow(ReminderType.All)
    val homePageReminderSortType: StateFlow<ReminderType> = _homePageReminderSortType

    fun changeBottomNavBarState(state: BottomNavBarState){
        _bottomNavBarState.update { state }
    }

    fun changeTopFilterBarState(state: TopFilterBarState){
        _topFilterBarState.update { state }
        when (state){
            is TopFilterBarState.JourneyState -> {
                when(state.type){
                    TopFilterBarJourneyType.Ascending -> {
                        _homePageJourneySort.update { JourneySortType.ASC }
                    }
                    TopFilterBarJourneyType.Descending -> {
                        _homePageJourneySort.update { JourneySortType.DESC }
                    }
                }
                if (_bottomNavBarState.value == BottomNavBarState.AddEditPage){
                    _addEditViewState.update { AddEditViewState.JOURNEY }
                }
            }
            is TopFilterBarState.ReminderState -> {
                when(state.type){
                    TopFilterBarReminderType.Alert -> {
                        _homePageReminderSortType.update { ReminderType.Alert }
                    }
                    TopFilterBarReminderType.Birthday -> {
                        _homePageReminderSortType.update { ReminderType.Birthday }
                    }
                    TopFilterBarReminderType.Event -> {
                        _homePageReminderSortType.update { ReminderType.Event }
                    }
                }
                if (_bottomNavBarState.value == BottomNavBarState.AddEditPage){
                    _addEditViewState.update { AddEditViewState.REMINDER }
                }
            }
        }
    }

}