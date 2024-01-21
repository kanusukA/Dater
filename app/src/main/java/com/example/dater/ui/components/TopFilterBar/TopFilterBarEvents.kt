package com.example.dater.ui.components.TopFilterBar

import com.example.dater.ui.components.BottomNavBar.BottomNavBarState

sealed class TopFilterBarEvents {
    data class ChangeSearchText(val string: String): TopFilterBarEvents()
    data class ChangeFilterType(val state: TopFilterBarState): TopFilterBarEvents()
    data class ChangeViewState(val state: BottomNavBarState): TopFilterBarEvents()
    object ChangeState: TopFilterBarEvents()
    object ToggleFilter: TopFilterBarEvents()
    object ToggleSearch: TopFilterBarEvents()
}