package com.example.dater.ui.components.TopFilterBar.util


sealed class TopFilterBarJourneyType {
    object Descending: TopFilterBarJourneyType()
    object Ascending: TopFilterBarJourneyType()
}

sealed class TopFilterBarReminderType {
    object All: TopFilterBarReminderType()
    object Event: TopFilterBarReminderType()
    object Birthday: TopFilterBarReminderType()
    object Alert: TopFilterBarReminderType()
}