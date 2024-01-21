package com.example.dater.ui.components.MyDatePicker

import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Reminder.domain.model.Reminder

sealed class MyDatePickerEvents {
    data class SetReminderDate(val reminder: Reminder): MyDatePickerEvents()
    data class SetJourneyDate( val journey: Journey): MyDatePickerEvents()
    data class Confirm(val long: Long): MyDatePickerEvents()
    object Dismiss: MyDatePickerEvents()
}