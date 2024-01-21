package com.example.dater.ui.components.TopFilterBar

import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType


sealed class TopFilterBarState {
    data class JourneyState(val type: TopFilterBarJourneyType): TopFilterBarState()
    data class ReminderState(val type: TopFilterBarReminderType): TopFilterBarState()
}