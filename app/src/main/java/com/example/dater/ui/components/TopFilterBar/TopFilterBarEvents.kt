package com.example.dater.ui.components.TopFilterBar

sealed class TopFilterBarEvents {
    data class ChangeSearchText(val string: String): TopFilterBarEvents()
    data class ChangeFilterType(val state: TopFilterBarType): TopFilterBarEvents()
}

sealed class TopFilterButtonEvents {
    object Search: TopFilterButtonEvents()
    object DisableSearch: TopFilterButtonEvents()

    object EnableSearch: TopFilterButtonEvents()
    object ToggleFilter: TopFilterButtonEvents()

    object ToggleState: TopFilterButtonEvents()

}