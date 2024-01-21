package com.example.dater.ui.addEditPage


sealed class AddEditErrors {
    data class TitleError(val string: String): AddEditErrors()
    data class StartDate(val long: Long): AddEditErrors()
    data class EndDate(val long: Long): AddEditErrors()
    data class ReminderDateEmpty(val boolean: Boolean): AddEditErrors()
    object TitleEmpty: AddEditErrors()
    object ResetAnimation: AddEditErrors()
}

data class EndDateError (
    val dateEmpty: Boolean = false,
    val dateLessThanStart: Boolean = false,
    val dateInvalid: Boolean = false
)

data class StartDateError (
    val dateEmpty: Boolean = false,
    val dateGreaterThanEnd: Boolean = false,
    val dateInvalid: Boolean = false
)

data class TitleErrors (
    val titleEmpty: Boolean = false,
    val titleLong: Boolean = false,
    val titleInvalid: Boolean = false
)