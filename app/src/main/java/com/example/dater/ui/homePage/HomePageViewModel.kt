package com.example.dater.ui.homePage

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.UiState.domain.repository.UiStateHomeRepository
import com.example.dater.Data.UiState.domain.repository.UiStateRepository
import com.example.dater.notification.ReminderNotifications
import com.example.dater.notification.reminderNotifier.ReminderNotifierRequest
import kotlinx.coroutines.flow.flatMapLatest


@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val journeyRepository: JourneyRepository,
    private val reminderRepository: ReminderRepository,
    private val uiState: UiStateRepository,
    private val uiStateHome: UiStateHomeRepository,
    private val app: Application
) : ViewModel() {

    private val reminderNotifier = ReminderNotifications(app.applicationContext)

    val topFilterBarState = uiState.getTopFilterBarState()

    private val _journeys = uiStateHome.getJourneySort()
    val journeys: StateFlow<List<Journey>> = _journeys
        .flatMapLatest { sort_type ->
            when(sort_type){
                JourneySortType.ASC -> journeyRepository.getJourneyByAsc()
                JourneySortType.DESC -> journeyRepository.getJourneyByDesc()
            }
        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())



    private val _reminders: StateFlow<ReminderType> = uiStateHome.getReminderSort()
    val reminders: StateFlow<List<Reminder>> = _reminders
        .flatMapLatest { filter ->
            if(filter == ReminderType.All){
                reminderRepository.getReminders()
            }else{
                reminderRepository.getRemindersByType(filter)
            }
        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList())

    private val _homePageState: MutableStateFlow<TopFilterBarState> = MutableStateFlow(TopFilterBarState.JourneyState(TopFilterBarJourneyType.Ascending))
    val homePageState: StateFlow<TopFilterBarState> = _homePageState

    init {
        viewModelScope.launch {
//            _journeysFlow.value = journeyRepository.getJourney().first()
        }
    }


    fun onEvent(events: HomePageEvents){

        when(events){

            is HomePageEvents.DeleteJourney -> {
                viewModelScope.launch {
                    journeyRepository.deleteJourney(events.journey)
                }
            }

            is HomePageEvents.DeleteReminder -> {
                val notifierRequest = ReminderNotifierRequest(app.applicationContext,events.reminder.id)
                notifierRequest.cancelReminderNotifier()
                viewModelScope.launch {
                    reminderRepository.deleteReminder(events.reminder)
                }
            }

            is HomePageEvents.EditJourney -> TODO()

            is HomePageEvents.ChangeState -> {

                _homePageState.update { events.homePageState }

            }

            is HomePageEvents.UpdateReminder -> {
                viewModelScope.launch {
                    reminderRepository.updateReminder(events.reminder)
                }
            }
        }

    }

    fun getReminders(journey: Journey): StateFlow<List<Reminder>>{

        val output = reminderRepository.getReminderList(journey.reminders).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

        return output
    }


}

enum class JourneySortType{
    ASC,
    DESC
}


