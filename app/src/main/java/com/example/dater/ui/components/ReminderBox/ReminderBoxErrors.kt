package com.example.dater.ui.components.ReminderBox

data class ReminderBoxErrors (
    val titleErrorLong: Boolean = false,
    val titleErrorEmpty: Boolean = false,
    val descriptionErrorLong: Boolean = false,
    val startDateError: Boolean = false,
    val endDateError: Boolean = false,
    val dateErrorEmpty: Boolean = false
)

sealed class ReminderBoxErrorEvents {
    data class Title(val string: String): ReminderBoxErrorEvents()
    data class Description(val string: String): ReminderBoxErrorEvents()
    data class StartDate(val long: Long): ReminderBoxErrorEvents()
    data class EndDate(val long: Long): ReminderBoxErrorEvents()
    object SaveCheck: ReminderBoxErrorEvents()
}
