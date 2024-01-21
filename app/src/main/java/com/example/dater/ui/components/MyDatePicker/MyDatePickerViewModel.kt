@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.dater.ui.components.MyDatePicker


import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MyDatePickerViewModel @Inject constructor():ViewModel() {

    private val _setDate = MutableStateFlow(0L)
    val setDate: StateFlow<Long> = _setDate

    val datePickerState = DatePickerState(
        initialSelectedDateMillis = _setDate.value,
        initialDisplayMode = DisplayMode.Picker,
        yearRange = DatePickerDefaults.YearRange,
        initialDisplayedMonthMillis = _setDate.value,
        locale = Locale.ENGLISH
    )

    private val _show = MutableStateFlow(false)
    val show: StateFlow<Boolean> = _show




    fun onEvent(events: MyDatePickerEvents){
        when(events){
            is MyDatePickerEvents.Confirm -> {
                _setDate.value = events.long
                _show.value = false
            }

            MyDatePickerEvents.Dismiss -> {
                _show.value = false
            }


            is MyDatePickerEvents.SetJourneyDate -> {
                _show.value = true

            }
            is MyDatePickerEvents.SetReminderDate -> TODO()
        }
    }
}