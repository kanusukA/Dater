package com.example.dater.ui.components.BottomNavBar

sealed class BottomNavBarState{
    object HomePage: BottomNavBarState()
    object AddEditPage: BottomNavBarState()
}
