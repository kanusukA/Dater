package com.example.dater.ui.components.BottomNavBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dater.ui.Navigation.NavRoutes
import com.example.dater.di.UiDependency.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class BottomNavBarViewModel @Inject constructor(
    private val navigationState: NavigationState
) : ViewModel(){

    private val _devTabShow = MutableStateFlow(false)
    val devTabShow: StateFlow<Boolean> = _devTabShow

    val bottomNavBarState: StateFlow<BottomNavBarState> = navigationState.navState.map {
        when (it){
            NavRoutes.AddEditPage -> {
                _devTabShow.update { false }
                BottomNavBarState.AddEditPage
            }
            NavRoutes.HomePage -> BottomNavBarState.HomePage
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = BottomNavBarState.HomePage
    )

    fun devTab(show: Boolean){
        if (show &&  bottomNavBarState.value != BottomNavBarState.AddEditPage){
            _devTabShow.update { show }
        } else {
            _devTabShow.update { show }
        }
    }


}

