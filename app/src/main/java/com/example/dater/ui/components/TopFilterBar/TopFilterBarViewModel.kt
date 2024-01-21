package com.example.dater.ui.components.TopFilterBar

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dater.Data.UiState.domain.model.UiState
import com.example.dater.Data.UiState.domain.repository.UiStateRepository
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TopFilterBarViewModel @Inject constructor(
    private val uiState: UiStateRepository
): ViewModel() {

    val state = uiState.getTopFilterBarState()

    val viewState = uiState.getBottomNavBarState()

    private val _stateString = MutableStateFlow("Journey")
    val stateString: StateFlow<String> = _stateString

    private val _search: MutableStateFlow<TopFilterBarSearch> =
        MutableStateFlow(TopFilterBarSearch("", "Search Journey"))
    val search: StateFlow<TopFilterBarSearch> = _search

    private val _toggleFilter = MutableStateFlow(false)
    val toggleFilter: StateFlow<Boolean> = _toggleFilter

    private val _toggleSearch = MutableStateFlow(false)
    val toggleSearch: StateFlow<Boolean> = _toggleSearch

    fun onEvent(events: TopFilterBarEvents) {
        when (events) {

            is TopFilterBarEvents.ChangeSearchText -> {
                _search.update { currentState -> currentState.copy(text = events.string) }
            }

            is TopFilterBarEvents.ChangeState -> {

                when (state.value) {

                    is TopFilterBarState.JourneyState -> {
                        if(viewState.value == BottomNavBarState.AddEditPage){
                            _toggleFilter.update { false }
                        }
                        uiState.changeTopFilterBarState(TopFilterBarState.ReminderState(TopFilterBarReminderType.Event))

                        _search.update { currentState -> currentState.copy(labelText = "Search Reminder") }
                        _stateString.value = "Reminder"


                    }

                    is TopFilterBarState.ReminderState -> {
                        if(viewState.value == BottomNavBarState.AddEditPage){
                            _toggleFilter.update { false }
                        }
                        uiState.changeTopFilterBarState(TopFilterBarState.JourneyState(TopFilterBarJourneyType.Descending))

                        _search.update { currentState -> currentState.copy(labelText = "Search Journey") }
                        _stateString.value = "Journey"


                    }
                }
            }

            TopFilterBarEvents.ToggleFilter -> {
                if (viewState.value == BottomNavBarState.HomePage) {
                    _toggleFilter.value = !_toggleFilter.value
                }
            }

            is TopFilterBarEvents.ChangeFilterType -> {
                uiState.changeTopFilterBarState(events.state)
            }

            TopFilterBarEvents.ToggleSearch -> {
                if(viewState.value == BottomNavBarState.HomePage){
                    _toggleSearch.value = !_toggleSearch.value
                }
            }


            //Redundant
            is TopFilterBarEvents.ChangeViewState -> {
                when(state.value){
                    is TopFilterBarState.JourneyState -> {
                        _toggleFilter.update { false }
                    }
                    is TopFilterBarState.ReminderState -> {
                        _toggleFilter.update { true }
                    }
                }

            }

        }

    }

}

