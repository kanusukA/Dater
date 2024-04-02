package com.example.dater.ui.components.TopFilterBar

import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType


sealed class TopFilterBarType {
    data class JourneyType(val type: TopFilterBarJourneyType): TopFilterBarType()
    data class ReminderType(val type: TopFilterBarReminderType): TopFilterBarType()
}

data class TopFilterBarSearchState(
    val showState: Boolean,
    val searchText: String
)