package com.example.dater.ui.components.ReminderBox

import com.example.dater.Data.Reminder.domain.model.ReminderDateType

sealed class ReminderBoxEvents {
    data class SelectedDays(val days: List<Int>): ReminderBoxEvents()
    data class StartDate(val long: Long): ReminderBoxEvents()
    data class EndDate(val long: Long): ReminderBoxEvents()
    data class Title(val string: String): ReminderBoxEvents()
    data class Edit(val boolean: Boolean): ReminderBoxEvents()
    data class Expand(val boolean: Boolean): ReminderBoxEvents()
    data class Description(val string: String): ReminderBoxEvents()
    data class DateType(val type: ReminderDateType): ReminderBoxEvents()
    data class ReminderType(val type: com.example.dater.Data.Reminder.utils.ReminderType): ReminderBoxEvents()
    object Cancel : ReminderBoxEvents()
    object Delete : ReminderBoxEvents()
    object Save : ReminderBoxEvents()
}