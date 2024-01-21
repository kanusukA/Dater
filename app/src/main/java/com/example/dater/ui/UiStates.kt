package com.example.dater.ui

import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UiStates {

    private val _topFilerBarState: MutableStateFlow<TopFilterBarState> = MutableStateFlow(TopFilterBarState.JourneyState(TopFilterBarJourneyType.Ascending))
    val topFilterBarState: StateFlow<TopFilterBarState> = _topFilerBarState

    private val _bottomNavBarState: MutableStateFlow<BottomNavBarState> = MutableStateFlow(BottomNavBarState.HomePage)
    val bottomNavBarState: StateFlow<BottomNavBarState> = _bottomNavBarState

    fun changeTopFilterBarState(state: TopFilterBarState){
        _topFilerBarState.value = state
    }

    fun changeBottomNavBarState(state: BottomNavBarState){
        _bottomNavBarState.value = state
    }
}