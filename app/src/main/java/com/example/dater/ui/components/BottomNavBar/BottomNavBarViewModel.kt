package com.example.dater.ui.components.BottomNavBar

import androidx.lifecycle.ViewModel
import com.example.dater.Data.UiState.domain.model.UiState
import com.example.dater.Data.UiState.domain.repository.UiStateRepository
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BottomNavBarViewModel @Inject constructor(
    private val uiState: UiStateRepository
): ViewModel(){

    val state:StateFlow<BottomNavBarState> = uiState.getBottomNavBarState()

    fun bottomNavBarEvents(events: BottomNavBarEvents){

        when(events){

            BottomNavBarEvents.CreateJourney -> {
                uiState.changeBottomNavBarState(BottomNavBarState.AddEditPage)

            }


        }

    }

}

