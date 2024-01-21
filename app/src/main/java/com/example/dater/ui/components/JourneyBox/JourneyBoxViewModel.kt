package com.example.dater.ui.components.JourneyBox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.Reminder.utils.getReminderIndex
import com.example.dater.Data.utils.DateHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class JourneyBoxViewModel(
    val journey: Journey,
    val deleteJourney: () -> Unit,
    private val reminders: List<Reminder>
) : ViewModel() {


    private val _listReminders = MutableStateFlow(reminders)
    val listReminders: StateFlow<List<Reminder>> = _listReminders

    private val _title = MutableStateFlow(journey.title)
    val title: StateFlow<String> = _title

    private val _startDate = MutableStateFlow(journey.startDate)
    val startDate: StateFlow<Long> = _startDate

    private val _endDate = MutableStateFlow(journey.endDate)
    val endDate: StateFlow<Long> = _endDate

    //UI -------------------------------------------------------------------------------------------
    private val _remindersDotCount = MutableStateFlow(
        ReminderDotCount(_listReminders.value.count { it.reminderType == ReminderType.Alert },
            _listReminders.value.count { it.reminderType == ReminderType.Event },
            _listReminders.value.count { it.reminderType == ReminderType.Birthday }
        )
    )
    val reminderDotCount: StateFlow<ReminderDotCount> = _remindersDotCount

    private val _selectedReminderType: MutableStateFlow<ReminderType> = MutableStateFlow(ReminderType.All)
    val selectedReminderType: StateFlow<ReminderType> = _selectedReminderType

    private val _selectedReminderIndex = MutableStateFlow(getReminderIndex(_selectedReminderType.value))
    val selectedReminderIndex: StateFlow<Int> = _selectedReminderIndex

    private val _startDateText = MutableStateFlow(DateHandler(_startDate.value).getText())
    val startDateText: StateFlow<String> = _startDateText

    private val _endDateText = MutableStateFlow(DateHandler(_endDate.value).getText())
    val endDateText: StateFlow<String> = _endDateText

    private val _lengthOfTime = MutableStateFlow((_endDate.value - _startDate.value) / 86400000L)
    val lengthOfTime: StateFlow<Long> = _lengthOfTime

    private val _timeLeft = MutableStateFlow((_endDate.value - DateHandler().getLong())/ 86400000L)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _expand = MutableStateFlow(false)
    val expand: StateFlow<Boolean> = _expand

    fun onEvent(events: JourneyBoxEvents) {
        when (events) {

            JourneyBoxEvents.DeleteJourney -> {
                deleteJourney()
            }

            JourneyBoxEvents.EditJourney -> TODO()

            JourneyBoxEvents.Expand -> {
                _expand.value = !_expand.value
            }

            is JourneyBoxEvents.SelectReminderType -> {
                if(_selectedReminderType.value == events.type && selectedReminderType.value != ReminderType.All){
                    _selectedReminderType.update { ReminderType.All }
                    _listReminders.update { reminders }
                }else{
                    _selectedReminderType.update { events.type }
                    when(events.type){
                        ReminderType.Alert -> {
                            _listReminders.update { reminders.filter { reminder -> reminder.reminderType == ReminderType.Alert } }
                        }
                        ReminderType.Birthday -> {
                            _listReminders.update { reminders.filter { reminder -> reminder.reminderType == ReminderType.Birthday } }
                        }
                        ReminderType.Event -> {
                            _listReminders.update { reminders.filter { reminder -> reminder.reminderType == ReminderType.Event } }
                        }

                        else -> {}
                    }
                }
                _selectedReminderIndex.update { getReminderIndex(_selectedReminderType.value) }
            }


        }
    }


}