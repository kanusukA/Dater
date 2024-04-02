package com.example.dater.ui.homePage

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import com.example.dater.ui.components.TopFilterBar.TopFilterBarType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.dater.Data.Journey.domain.model.JourneyWidgetSelection
import com.example.dater.Data.Journey.utils.JourneyWidgetType
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.di.UiDependency.TopFilterBarState
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val journeyRepository: JourneyRepository,
    private val reminderRepository: ReminderRepository,
    private val topFilterBarState: TopFilterBarState,
    private val app: Application
) : ViewModel() {

    private val journeyFlow = journeyRepository.getJourney()

    private val reminderFlow = reminderRepository.getReminders()

    private val _journeyFiltered: Flow<List<Journey>> = topFilterBarState.searchState
        .combine(journeyFlow){searchState, journey ->

            when(searchState.searchText.isNotBlank()){
                true -> {
                    journey.filter { it.title.contains(searchState.searchText,true) }
                }
                false -> {
                    journey
                }
            }
        }

    val journeys = topFilterBarState.state
        .combine(_journeyFiltered){ state, journeys ->

            when(state){
                is TopFilterBarType.JourneyType -> {
                    when (state.type){
                        TopFilterBarJourneyType.Ascending -> journeys.sortedBy { it.endDate }
                        TopFilterBarJourneyType.Descending -> journeys.sortedByDescending { it.endDate }
                    }
                }
                is TopFilterBarType.ReminderType -> journeys
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val reminders = topFilterBarState.state
        .combine(reminderFlow){ state, reminders ->
            when (state){
                is TopFilterBarType.JourneyType -> {
                    reminders
                }
                is TopFilterBarType.ReminderType -> {
                    when(state.type){
                        TopFilterBarReminderType.Alert -> {reminders.filter { it.reminderType == ReminderType.Alert }}
                        TopFilterBarReminderType.All -> {reminders}
                        TopFilterBarReminderType.Birthday -> {reminders.filter { it.reminderType == ReminderType.Birthday }}
                        TopFilterBarReminderType.Event -> {reminders.filter { it.reminderType == ReminderType.Event }}
                    }
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _journeyWidgetSelection = journeyRepository.getJourneySelection()
    val journeyWidgetSelection: StateFlow<JourneyWidgetSelection> = _journeyWidgetSelection.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = JourneyWidgetSelection()
    )


    val homePageState: StateFlow<TopFilterBarType> = topFilterBarState.state.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TopFilterBarType.JourneyType(TopFilterBarJourneyType.Ascending)
    )


    fun onEvent(events: HomePageEvents){

        when(events){

            is HomePageEvents.DeleteJourney -> {
                viewModelScope.launch {
                    viewModelScope.launch{
                        if (events.listOfReminders.isNotEmpty()) {
                            events.listOfReminders.forEach {
                                reminderRepository.deleteReminder(it, context = app.applicationContext)
                            }
                        }
                        journeyRepository.deleteJourney(events.journey)
                    }
                }
            }

            is HomePageEvents.DeleteReminder -> {
                viewModelScope.launch {
                    reminderRepository.deleteReminder(events.reminder, context = app.applicationContext)
                }
            }

            is HomePageEvents.EditJourney -> {
                //TODO(MORE OPTIMIZED UPDATE)
                viewModelScope.launch {
                    journeyRepository.updateJourney(events.journey)
                }
            }

            is HomePageEvents.UpdateReminder -> {
                viewModelScope.launch {
                    reminderRepository.updateReminder(events.reminder)
                }
            }

            is HomePageEvents.ChangeJourneyWidgetType -> {

                viewModelScope.launch{
                    when (events.journeyWidgetType) {
                        JourneyWidgetType.None -> {
                            if (journeyWidgetSelection.value.primaryJourneyId == -1) {
                                journeyRepository.setJourneySelection(
                                    journeyWidgetSelection.value.copy(
                                        primaryJourneyId = events.journey.id
                                    )
                                )
                            } else {
                                journeyRepository.setJourneySelection(
                                    journeyWidgetSelection.value.copy(
                                        secondaryJourneyId = events.journey.id
                                    )
                                )
                            }
                        }

                        JourneyWidgetType.PrimarySelection -> {
                            journeyRepository.setJourneySelection(
                                journeyWidgetSelection.value.copy(
                                    primaryJourneyId = -1
                                )
                            )
                        }

                        JourneyWidgetType.SecondarySelection -> {
                            journeyRepository.setJourneySelection(
                                journeyWidgetSelection.value.copy(
                                    secondaryJourneyId = -1
                                )
                            )
                        }
                    }
                }

            }

            is HomePageEvents.ForceJourneyWidgetToPrimary -> {
                viewModelScope.launch{
                    journeyRepository.setJourneySelection(
                        journeyWidgetSelection.value.copy(
                            primaryJourneyId = events.journey.id
                        )
                    )
                }
            }
        }

    }

    fun getReminders(journey: Journey): StateFlow<List<Reminder>> {

        return reminderRepository.getReminderList(journey.reminders).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    }


}




