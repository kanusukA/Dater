@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dater.ui.addEditPage


import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType


sealed class AddEditEvents {
    data class ChangeTitle(val string: String): AddEditEvents()
    data class ChangeReminder(val reminder: Reminder): AddEditEvents()
    data class ChangeReminderInList(val index: Int,val reminder: Reminder): AddEditEvents()
    data class DeleteReminderInList(val index: Int): AddEditEvents()
    data class showDatePicker(val show: Boolean = false): AddEditEvents()
    data class SetDatePicker(val dateType: DateType, val long: Long): AddEditEvents()
    object AddReminder: AddEditEvents()
}

sealed class AddEditUiEvents {
    data class ChangeState(val state: AddEditViewState): AddEditUiEvents()
}

sealed class DateType {
    data class StartDate(val state: AddEditViewState): DateType()
    data class EndDate(val state: AddEditViewState): DateType()
}

sealed class AddEditBottomNavBarEvents {
    object Save: AddEditBottomNavBarEvents()
    object Cancel: AddEditBottomNavBarEvents()
}