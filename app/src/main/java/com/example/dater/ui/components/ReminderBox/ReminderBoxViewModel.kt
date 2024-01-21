package com.example.dater.ui.components.ReminderBox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.model.ReminderDateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class ReminderBoxViewModel(
    private val reminder: Reminder,
    val editable: Boolean,
    val expandable: Boolean,
    private val initialEditState: Boolean,
    private val initialExpandState: Boolean,
    private val onSaveReminder: (Reminder) -> Unit,
    private val onDeleteReminder: () -> Unit,

    ) : ViewModel() {

    private val _savedReminder = MutableStateFlow(reminder)
    val savedReminder: StateFlow<Reminder> = _savedReminder

    private val _editState = MutableStateFlow(initialEditState)
    val editState: StateFlow<Boolean> = _editState

    private val _expandState = MutableStateFlow(initialExpandState)
    val expandState:StateFlow<Boolean> = _expandState

    private val _errorHandler = MutableStateFlow(ReminderBoxErrors())
    val errorHandler: StateFlow<ReminderBoxErrors> = _errorHandler

    fun onEvent(events: ReminderBoxEvents) {
        when (events) {

            is ReminderBoxEvents.DateType -> {
                _savedReminder.update { reminder -> reminder.copy(dateType = events.type) }
            }

            ReminderBoxEvents.Cancel -> {
                _savedReminder.update { reminder -> reminder }
                _editState.update { false }
                _expandState.update { false }
            }

            ReminderBoxEvents.Delete -> {
                onDeleteReminder()
            }

            is ReminderBoxEvents.Description -> {
                onError(ReminderBoxErrorEvents.Description(events.string))
                if (!errorHandler.value.descriptionErrorLong){
                    _savedReminder.update { reminder -> reminder.copy(description = events.string) }
                }
            }

            is ReminderBoxEvents.EndDate -> {
                onError(ReminderBoxErrorEvents.EndDate(events.long))
                if (!errorHandler.value.endDateError){
                    _savedReminder.update { reminder -> reminder.copy(endDate = events.long) }
                }
            }

            is ReminderBoxEvents.ReminderType -> {
                _savedReminder.update { reminder -> reminder.copy(reminderType = events.type) }
            }

            ReminderBoxEvents.Save -> {
                onError(ReminderBoxErrorEvents.SaveCheck)
                if (errorHandler.value.titleErrorEmpty || errorHandler.value.dateErrorEmpty){
                    // Error
                } else {
                    onSaveReminder(_savedReminder.value)
                    _editState.update { false }
                    _expandState.update { false }
                }
            }

            is ReminderBoxEvents.StartDate -> {
                onError(ReminderBoxErrorEvents.StartDate(events.long))
                if (!errorHandler.value.startDateError){
                    _savedReminder.update { reminder -> reminder.copy(startDate = events.long) }
                }
            }

            is ReminderBoxEvents.Title -> {
                onError(ReminderBoxErrorEvents.Title(events.string))
                if (!errorHandler.value.titleErrorLong){
                    _savedReminder.update { reminder -> reminder.copy(title = events.string) }
                }
            }

            is ReminderBoxEvents.Edit -> {
                if (editable){ _editState.update { events.boolean } }
            }

            is ReminderBoxEvents.Expand -> {
                if(expandable){ _expandState.update { events.boolean } }
            }

            is ReminderBoxEvents.SelectedDays -> {
                _savedReminder.update { reminder -> reminder.copy(selectedDays = events.days) }
                _errorHandler.update { errors -> errors.copy(dateErrorEmpty = false) }
            }
        }
    }

    // Error Handling -------------------------------------------------------------------------------

    private fun onError(events: ReminderBoxErrorEvents){
        when (events){
            is ReminderBoxErrorEvents.Title -> {

                if (events.string.length > 25){
                    _errorHandler.update { errors -> errors.copy(titleErrorLong = true) }
                }else{
                    _errorHandler.update { errors -> errors.copy(titleErrorLong = false) }
                    _errorHandler.update { errors -> errors.copy(titleErrorEmpty = false) }
                }
            }
            is ReminderBoxErrorEvents.Description -> {

                if (events.string.length > 150){
                    _errorHandler.update { errors -> errors.copy(descriptionErrorLong = true) }
                } else {
                    _errorHandler.update { errors -> errors.copy(descriptionErrorLong = false) }
                }

            }
            is ReminderBoxErrorEvents.StartDate -> {

                if (events.long >= _savedReminder.value.endDate && _savedReminder.value.endDate != 0L){
                    _errorHandler.update { errors -> errors.copy(startDateError = true) }
                }else {
                    _errorHandler.update { errors -> errors.copy(startDateError = false) }
                    _errorHandler.update { errors -> errors.copy(dateErrorEmpty = false) }
                }

            }
            is ReminderBoxErrorEvents.EndDate -> {

                if (events.long <= _savedReminder.value.startDate && _savedReminder.value.startDate != 0L){
                    _errorHandler.update { errors -> errors.copy(endDateError = true) }
                } else {
                    _errorHandler.update { errors -> errors.copy(endDateError = false) }
                    _errorHandler.update { errors -> errors.copy(dateErrorEmpty = false) }
                }

            }

            is ReminderBoxErrorEvents.SaveCheck -> {
                if (savedReminder.value.title.isBlank()){
                    _errorHandler.update { errors -> errors.copy(titleErrorEmpty = true) }
                }

                when (savedReminder.value.dateType){

                    ReminderDateType.EmptyDate -> {/*DO NOTHING*/}

                    ReminderDateType.SelectedDate -> {

                        if (savedReminder.value.startDate == 0L && savedReminder.value.startDate == 0L){
                            _errorHandler.update { errors -> errors.copy(dateErrorEmpty = true) }
                        }

                    }

                    ReminderDateType.SelectedDays -> {
                        if (!savedReminder.value.selectedDays.contains(1)){
                            _errorHandler.update { errors ->  errors.copy(dateErrorEmpty = true) }
                        }
                    }
                }
            }


        }
    }

}

@Suppress("UNCHECKED_CAST")
class ReminderBoxViewModelFactory(
    private val reminder: Reminder,
    val editable: Boolean,
    val expandable: Boolean,
    val initialEditState: Boolean,
    val initialExpandState: Boolean,
    private val onSaveReminder: (Reminder) -> Unit,
    private val onDeleteReminder: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReminderBoxViewModel::class.java)){
            return ReminderBoxViewModel(reminder, editable, expandable,initialEditState,initialExpandState, onSaveReminder, onDeleteReminder) as T
        }
        throw IllegalArgumentException("Error in Factory")
    }
}