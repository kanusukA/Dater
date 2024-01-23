package com.example.dater.ui.components.ReminderBox

import android.content.Context
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.model.ReminderDateType
import com.example.dater.ToastMessage



sealed class ReminderBoxErrorEvents {
    data class Title(val string: String): ReminderBoxErrorEvents()
    data class Description(val string: String): ReminderBoxErrorEvents()
    data class StartDate(val long: Long): ReminderBoxErrorEvents()
    data class EndDate(val long: Long): ReminderBoxErrorEvents()
    object SaveCheck: ReminderBoxErrorEvents()
}

class ReminderErrorCheck(
    private val context: Context,
    private val reminder: Reminder,
    val onUpdateReminder: (Reminder) -> Unit,
    val onSaveReminder: (Reminder) -> Unit
){
    fun title(string: String){
        if (string.length < 25){
            onUpdateReminder(reminder.copy(title = string))
        } else{
            ToastMessage(context, " Title Long")
        }
    }

    fun description(string: String){
        if (string.length < 150){
            onUpdateReminder(reminder.copy(description = string))
        } else {
            ToastMessage(context, "Description Long")
        }
    }

    fun startDate(long: Long){
        if (reminder.endDate > long || reminder.endDate == 0L){
            onUpdateReminder(reminder.copy(startDate = long))
        } else {
            ToastMessage(context, "Invalid Date")
        }
    }

    fun endDate(long: Long){
        if (reminder.startDate < long || reminder.startDate == 0L){
            onUpdateReminder(reminder.copy(endDate = long))
        } else {
            ToastMessage(context, "Invalid Date")
        }
    }

    fun saveReminder(){
        if (reminder.title.isBlank()){
            ToastMessage(context,"Title empty")
        } else if (reminder.dateType == ReminderDateType.SelectedDays && !reminder.selectedDays.contains(1)){
            ToastMessage(context, "No Days Selected")
        } else if (reminder.endDate == 0L && reminder.startDate == 0L && reminder.dateType != ReminderDateType.SelectedDays){
            ToastMessage(context,"Date Empty")
        } else {
            onSaveReminder(reminder)
        }

    }

}
