package com.example.dater.ui.components.JourneyBox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.Reminder.utils.getReminderIndex
import com.example.dater.Data.utils.DateHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class JourneyBoxViewModel(
    val journey: Journey,
    val notification: Boolean,
    val deleteJourney: (List<Reminder>) -> Unit,
    val onChangeJourneyNotificationState: (Journey) -> Unit,
    private val reminders: StateFlow<List<Reminder>>
) : ViewModel() {

    private val _title = MutableStateFlow(journey.title)
    val title: StateFlow<String> = _title

    private val _startDate = MutableStateFlow(journey.startDate)
    val startDate: StateFlow<Long> = _startDate

    private val _endDate = MutableStateFlow(journey.endDate)
    val endDate: StateFlow<Long> = _endDate

    private val _selectedReminderType: MutableStateFlow<ReminderType> = MutableStateFlow(ReminderType.All)

    private val _startDateText = MutableStateFlow(DateHandler(_startDate.value).getText())
    val startDateText: StateFlow<String> = _startDateText

    private val _endDateText = MutableStateFlow(DateHandler(_endDate.value).getText())
    val endDateText: StateFlow<String> = _endDateText

    private val _timeLeft = MutableStateFlow((_endDate.value - DateHandler().getLong())/ 86400000L)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _expand = MutableStateFlow(false)
    val expand: StateFlow<Boolean> = _expand

    private val _alertState = MutableStateFlow(notification)
    val alertState: StateFlow<Boolean> = _alertState

    // UI --------------------------------------------------------------------------------------

    val listReminders = _selectedReminderType
        .combine(reminders){ reminderType, reminders ->
            when (reminderType){
                ReminderType.Alert -> reminders.filter {reminder -> reminder.reminderType == ReminderType.Alert }
                ReminderType.All -> reminders
                ReminderType.Birthday -> reminders.filter { reminder -> reminder.reminderType == ReminderType.Birthday }
                ReminderType.Event -> reminders.filter { reminder -> reminder.reminderType == ReminderType.Event }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val selectedReminderIndex = _selectedReminderType.mapLatest {
        getReminderIndex(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    fun onEvent(events: JourneyBoxEvents) {
        when (events) {

            JourneyBoxEvents.DeleteJourney -> {
                deleteJourney(listReminders.value)
            }

            JourneyBoxEvents.EditJourney -> TODO()

            JourneyBoxEvents.Expand -> {
                _expand.value = !_expand.value
            }

            is JourneyBoxEvents.SelectReminderType -> {

                if(_selectedReminderType.value == events.type && _selectedReminderType.value != ReminderType.All){
                    _selectedReminderType.update { ReminderType.All }
                }else{
                    _selectedReminderType.update { events.type }
                }
            }

            JourneyBoxEvents.AlertState -> {
                _alertState.update { !_alertState.value }
                onChangeJourneyNotificationState(journey.copy(notification = _alertState.value))

            }
        }
    }
}
