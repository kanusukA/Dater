package com.example.dater.Data.UiState.dataSource.repository

import com.example.dater.Data.UiState.domain.model.UiState
import com.example.dater.Data.UiState.domain.repository.UiStateRepository
import com.example.dater.ui.addEditPage.AddEditEvents
import com.example.dater.ui.addEditPage.AddEditViewState
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import kotlinx.coroutines.flow.StateFlow

class UiStateRepoImpli(
    private val uiState: UiState
): UiStateRepository {
    override fun getBottomNavBarState(): StateFlow<BottomNavBarState> {
        return uiState.bottomNavBarState
    }

    override fun getTopFilterBarState(): StateFlow<TopFilterBarState> {
        return uiState.topFilterBarState
    }

    override fun getAddEditViewState(): StateFlow<AddEditViewState> {
        return uiState.addEditViewState
    }

    override fun changeBottomNavBarState(state: BottomNavBarState) {
        uiState.changeBottomNavBarState(state)
    }

    override fun changeTopFilterBarState(state: TopFilterBarState) {
        uiState.changeTopFilterBarState(state)
    }


}