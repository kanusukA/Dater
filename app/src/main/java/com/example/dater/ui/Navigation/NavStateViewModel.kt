package com.example.dater.ui.Navigation

import androidx.lifecycle.ViewModel
import com.example.dater.di.UiDependency.NavigationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavStateViewModel @Inject constructor(
    private val navigationState: NavigationState
): ViewModel() {
    fun changeNavState(navRoutes: NavRoutes){
        navigationState.changeNavigation(navRoutes)
    }
}