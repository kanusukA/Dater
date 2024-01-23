package com.example.dater.ui.addEditPage


import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Journey.domain.repository.JourneyRepository
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.model.ReminderDateType
import com.example.dater.Data.Reminder.domain.repository.ReminderRepository
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.UiState.domain.repository.UiStateRepository
import com.example.dater.Data.utils.DateHandler
import com.example.dater.ToastMessage
import com.example.dater.notification.reminderNotifier.ReminderNotifierRequest
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val journeyRepository: JourneyRepository,
    private val reminderRepository: ReminderRepository,
    private val app: Application,
    private val uiState: UiStateRepository,

    ) : ViewModel() {


    val addEditViewState: StateFlow<AddEditViewState> = uiState.getAddEditViewState()

    // Reminder
    private val _reminder: MutableStateFlow<Reminder> =
        MutableStateFlow(Reminder(0L, 0L, "", "", ReminderType.Event))
    val reminder: StateFlow<Reminder> = _reminder

    // Journey
    private val _journey: MutableStateFlow<Journey> =
        MutableStateFlow(Journey(0L, 0L, emptyList(), ""))
    val journey: StateFlow<Journey> = _journey

    private val _reminderList: MutableStateFlow<List<Reminder>> =
        MutableStateFlow(listOf())
    val reminderList: StateFlow<List<Reminder>> = _reminderList

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker

    //Ui Variables----------------------------------------------------------------------------------

    val startDateText = _journey.map {
        if (it.startDate == 0L) {
            "DD/MM/YY"
        } else {
            DateHandler(it.startDate).getText()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "DD/MM/YY"
    )

    val endDateText = _journey.map {
        if (it.endDate == 0L) {
            "DD/MM/YY"
        } else {
            DateHandler(it.endDate).getText()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "DD/MM/YY"
    )

    val reminderStartDateEmpty = _reminder.map {
        it.startDate == 0L
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )

    val reminderEndDateEmpty = _reminder.map {
        it.endDate == 0L
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )

    val reminderStartDateText = _reminder.map {
        if (it.startDate == 0L) {
            "DD/MM/YY"
        } else {
            DateHandler(it.startDate).getText()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "DD/MM/YY"
    )

    val reminderEndDateText = _reminder.map {
        if (it.endDate == 0L) {
            "DD/MM/YY"
        } else {
            DateHandler(it.endDate).getText()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = "DD/MM/YY"
    )


    //Errors ---------------------------------------------------------------------------------------
    private val _titleErrors = MutableStateFlow(TitleErrors())
    val titleErrors: StateFlow<TitleErrors> = _titleErrors

    private val _startDateErrors = MutableStateFlow(StartDateError())
    val startDateError: StateFlow<StartDateError> = _startDateErrors

    private val _endDateErrors = MutableStateFlow(EndDateError())
    val endDateError: StateFlow<EndDateError> = _endDateErrors

    private val _reminderDateErrorEmpty = MutableStateFlow(false)
    val reminderDateErrorEmpty: StateFlow<Boolean> = _reminderDateErrorEmpty


    //Functions ------------------------------------------------------------------------------------
    //ON EVENT -------------------------------------------------------------------------------------

    fun onEvent(events: AddEditEvents) {

        when (events) {
            is AddEditEvents.ChangeTitle -> {

                onError(AddEditErrors.TitleError(events.string))
                if (!_titleErrors.value.titleLong) {
                    _journey.update { currentTitle -> currentTitle.copy(title = events.string) }
                }

            }

            AddEditEvents.AddReminder -> {

                val outputList = _reminderList.value.toMutableList()
                    .plus(Reminder(0L, 0L, "", "", ReminderType.Event))
                _reminderList.update { outputList }
            }

            is AddEditEvents.showDatePicker -> {
                _showDatePicker.update { events.show }
            }

            is AddEditEvents.SetDatePicker -> {

                _showDatePicker.update { false }

                when (events.dateType) {
                    // CHANGES ENDING JOURNEY DATE-------
                    is DateType.EndDate -> {
                        when (events.dateType.state) {

                            AddEditViewState.JOURNEY -> {

                                onError(AddEditErrors.EndDate(events.long))

                                if (!endDateError.value.dateLessThanStart) {
                                    if (journey.value.startDate == 0L) {
                                        _journey.update { currentState ->
                                            currentState.copy(
                                                startDate = DateHandler().getLong()
                                            )
                                        }
                                    }
                                    _journey.update { currentState -> currentState.copy(endDate = events.long) }
                                }


                            }


                            AddEditViewState.REMINDER -> {
                                if (events.long < reminder.value.startDate) {
//                                    TODO()
                                } else {
                                    if (reminder.value.startDate == 0L) {
                                        _reminder.update { currentState ->
                                            currentState.copy(
                                                startDate = DateHandler().getLong()
                                            )
                                        }
                                    }
                                    _reminder.update { currentState -> currentState.copy(endDate = events.long) }
                                }
                            }
                        }

                    }
                    // CHANGES STARTING JOURNEY DATE-------
                    is DateType.StartDate -> {

                        when (events.dateType.state) {

                            AddEditViewState.JOURNEY -> {
                                onError(AddEditErrors.StartDate(events.long))
                                if (!startDateError.value.dateGreaterThanEnd) {
                                    _journey.update { currentState -> currentState.copy(startDate = events.long) }
                                }
                            }

                            AddEditViewState.REMINDER -> {
                                if (events.long > reminder.value.endDate && reminder.value.endDate != 0L) {
//                                    TODO()
                                } else {
                                    _reminder.update { currentState -> currentState.copy(startDate = events.long) }
                                }
                            }
                        }
                    }
                }
            }

            is AddEditEvents.ChangeReminderInList -> {
                val outputList = _reminderList.value.toMutableList()
                outputList[events.index] = events.reminder
                _reminderList.update { outputList }
            }

            is AddEditEvents.DeleteReminderInList -> {
                val outputList = _reminderList.value.toMutableList()
                outputList.removeAt(events.index)
                _reminderList.update { outputList }
            }

            is AddEditEvents.ChangeReminder -> {
                _reminder.update { events.reminder }
            }
        }
    }

    //BOTTOM NAV BAR -------------------------------------------------------------------------------

    fun onBottomNavBarEvents(events: AddEditBottomNavBarEvents) {

        when (events) {

            AddEditBottomNavBarEvents.Cancel -> {
                uiState.changeBottomNavBarState(BottomNavBarState.HomePage)
            }

            AddEditBottomNavBarEvents.Save -> {

                when (addEditViewState.value) {

                    AddEditViewState.JOURNEY -> {
                        if (_journey.value.title.isBlank()) {
                            onError(AddEditErrors.TitleEmpty)
                        } else {
                            val journeyReminders = mutableListOf<Long>()

                            viewModelScope.launch {

                                _reminderList.value.forEach {

                                    val reminderId = reminderRepository.insertReminder(it.copy())

                                    journeyReminders.add(reminderId)

                                    addReminderNotification(
                                        app.applicationContext,
                                        reminderId = reminderId.toInt(),
                                        reminder = it
                                    )

                                    _journey.update { currentState -> currentState.copy(reminders = journeyReminders) }

                                }

                                viewModelScope.launch {
                                    journeyRepository.insertJourney(_journey.value.copy())
                                }

                            }
                            uiState.changeBottomNavBarState(BottomNavBarState.HomePage)
                        }

                    }

                    AddEditViewState.REMINDER -> {
                        if (_reminder.value.title.isBlank()) {
                            onError(AddEditErrors.TitleEmpty)
                        } else {

                            viewModelScope.launch {
                                val reminderId = reminderRepository.insertReminder(_reminder.value)
                                addReminderNotification(app.applicationContext,reminderId.toInt(),_reminder.value)
                            }

                            uiState.changeBottomNavBarState(BottomNavBarState.HomePage)
                        }
                    }
                }


            }

        }
    }

    //ERROR ----------------------------------------------------------------------------------------

    private fun onError(errors: AddEditErrors) {

        when (errors) {
            is AddEditErrors.ResetAnimation -> {
                _startDateErrors.update { currentState -> currentState.copy(dateGreaterThanEnd = false) }
                _endDateErrors.update { currentState -> currentState.copy(dateLessThanStart = false) }
            }

            is AddEditErrors.StartDate -> {
                if (_journey.value.endDate != 0L) {
                    if (errors.long >= _journey.value.endDate) {
                        _startDateErrors.update { currentState ->
                            currentState.copy(
                                dateGreaterThanEnd = true
                            )
                        }
                        ToastMessage(app.applicationContext, "Date is Greater Than End Date")
                    } else {
                        _startDateErrors.update { currentState ->
                            currentState.copy(
                                dateGreaterThanEnd = false
                            )
                        }
                    }
                } else {
                    _startDateErrors.update { currentState -> currentState.copy(dateGreaterThanEnd = false) }
                }
            }

            is AddEditErrors.EndDate -> {
                if (_journey.value.startDate != 0L) {
                    if (errors.long <= _journey.value.startDate) {
                        _endDateErrors.update { currentState -> currentState.copy(dateLessThanStart = true) }
                        ToastMessage(app.applicationContext, "Date is Less Than Start Date")
                    } else {
                        _endDateErrors.update { currentState -> currentState.copy(dateLessThanStart = false) }
                    }
                } else {
                    _endDateErrors.update { currentState -> currentState.copy(dateLessThanStart = false) }
                }
            }

            is AddEditErrors.TitleEmpty -> {
                _titleErrors.update { currentState -> currentState.copy(titleEmpty = true) }
                ToastMessage(app.applicationContext, "Title is Empty!")
            }

            is AddEditErrors.TitleError -> {

                if (errors.string.length > 40) {
                    _titleErrors.update { currentState -> currentState.copy(titleLong = true) }
                } else {
                    _titleErrors.update { currentState -> currentState.copy(titleLong = false) }
                }
            }

            is AddEditErrors.ReminderDateEmpty -> {
                _reminderDateErrorEmpty.update { errors.boolean }
            }

        }

    }

    private fun addReminderNotification(
        context: Context,
        reminderId: Int,
        reminder: Reminder
    ) {

        val notifierRequest = ReminderNotifierRequest(context, reminderId)

        when (reminder.dateType) {
            ReminderDateType.EmptyDate -> {

                notifierRequest.createPeriodicReminderNotifierRequest(
                    title = reminder.title,
                    text = reminder.description,
                    end = reminder.endDate
                )

            }

            ReminderDateType.SelectedDate -> {
                if (reminder.endDate == 0L) {

                    notifierRequest.createOneTimeReminderNotifierRequest(
                        title = reminder.title,
                        text = reminder.description,
                        delay = Duration.ofMillis(reminder.startDate - DateHandler().getLong())
                    )

                } else {

                    notifierRequest.createPeriodicReminderNotifierRequest(
                        title = reminder.title,
                        startFrom = reminder.startDate,
                        text = reminder.description,
                        end = reminder.endDate
                    )

                }

            }

            ReminderDateType.SelectedDays -> {

                notifierRequest.createWeeklyReminderNotifierRequest(
                    reminder.selectedDays,
                    reminder.title,
                    reminder.description
                )

            }
        }
    }

    fun clear(){
        _journey.update { Journey(0L,0L, title = "") }
        _reminder.update { Reminder(0L, 0L, "", "", ReminderType.Event) }
        _reminderList.update { listOf() }
    }

}


sealed class AddEditViewState {
    object JOURNEY : AddEditViewState()
    object REMINDER : AddEditViewState()
}