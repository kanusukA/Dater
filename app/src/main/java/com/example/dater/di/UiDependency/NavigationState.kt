package com.example.dater.di.UiDependency

import com.example.dater.ui.Navigation.NavRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationState @Inject constructor(){

    private val _navState: MutableStateFlow<NavRoutes> = MutableStateFlow(NavRoutes.HomePage)
    val navState : StateFlow<NavRoutes> = _navState

    fun changeNavigation(navRoute: NavRoutes){
        _navState.update { navRoute }
    }
}