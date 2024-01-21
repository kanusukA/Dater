package com.example.dater.Data.UiState.domain.repository

import com.example.dater.ui.addEditPage.AddEditViewState
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import kotlinx.coroutines.flow.StateFlow

interface UiStateRepository {

    fun getBottomNavBarState(): StateFlow<BottomNavBarState>

    fun getTopFilterBarState(): StateFlow<TopFilterBarState>

    fun getAddEditViewState(): StateFlow<AddEditViewState>

    fun changeBottomNavBarState(state: BottomNavBarState)

    fun changeTopFilterBarState(state: TopFilterBarState)
}