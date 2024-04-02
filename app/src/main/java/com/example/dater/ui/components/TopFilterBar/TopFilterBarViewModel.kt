package com.example.dater.ui.components.TopFilterBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dater.di.UiDependency.NavigationState
import com.example.dater.di.UiDependency.TopFilterBarState
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TopFilterBarViewModel @Inject constructor(
    private val navigationState: NavigationState,
    private val topFilterBarState: TopFilterBarState
) : ViewModel() {

    val state = topFilterBarState.state.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TopFilterBarType.JourneyType(TopFilterBarJourneyType.Ascending)
    )

    val searchState = topFilterBarState.searchState.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TopFilterBarSearchState(false,"")
    )


    private val _toggleFilter = MutableStateFlow(false)
    val toggleFilter: StateFlow<Boolean> = _toggleFilter

    // UI ------------------------------------------------------------------------------------------

    val selected: StateFlow<Int> = topFilterBarState.state.map {
        when (it) {
            is TopFilterBarType.JourneyType -> {
                when (it.type) {
                    TopFilterBarJourneyType.Ascending -> 0
                    TopFilterBarJourneyType.Descending -> 1
                }
            }

            is TopFilterBarType.ReminderType -> {
                when (it.type) {
                    TopFilterBarReminderType.Alert -> 0
                    TopFilterBarReminderType.All -> 0
                    TopFilterBarReminderType.Birthday -> 1
                    TopFilterBarReminderType.Event -> 2
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    val stateString: StateFlow<String> = topFilterBarState.state.map {
        when (it) {
            is TopFilterBarType.JourneyType -> "Journey"
            is TopFilterBarType.ReminderType -> "Reminder"
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "Journey"
    )

    // reminder filter not working!!!!
    fun onEvent(events: TopFilterBarEvents) {
        when (events) {

            is TopFilterBarEvents.ChangeSearchText -> {
                topFilterBarState.changeSearchText(events.string)
            }


            is TopFilterBarEvents.ChangeFilterType -> {

                when (events.state){

                    is TopFilterBarType.JourneyType -> topFilterBarState.changeState(events.state)

                    is TopFilterBarType.ReminderType -> {
                        if (topFilterBarState.state.value == events.state){
                            topFilterBarState.changeState(TopFilterBarType.ReminderType(TopFilterBarReminderType.All))
                        } else {
                            topFilterBarState.changeState(events.state)
                        }
                    }

                }

            }

        }

    }

    fun onButtonEvents(events: TopFilterButtonEvents){
        when (events){
            TopFilterButtonEvents.DisableSearch -> {
                topFilterBarState.changeSearchState(boolean = false)
                topFilterBarState.changeSearchText("")
            }
            TopFilterButtonEvents.Search -> {
                topFilterBarState.changeSearchText(searchState.value.searchText)
            }
            TopFilterButtonEvents.ToggleFilter -> {
                _toggleFilter.update { !it }
            }

            TopFilterButtonEvents.ToggleState -> {
                when (state.value){
                    is TopFilterBarType.JourneyType -> {
                        topFilterBarState.changeState(TopFilterBarType.ReminderType(TopFilterBarReminderType.All))
                    }
                    is TopFilterBarType.ReminderType -> {
                        topFilterBarState.changeState(TopFilterBarType.JourneyType(TopFilterBarJourneyType.Ascending))
                    }
                }
            }

            TopFilterButtonEvents.EnableSearch -> {
                topFilterBarState.changeSearchState(boolean = true)
            }
        }
    }


}


