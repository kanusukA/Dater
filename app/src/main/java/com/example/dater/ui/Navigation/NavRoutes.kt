package com.example.dater.ui.Navigation

sealed class NavRoutes(val routes: String){
    object HomePage: NavRoutes("home_page")
    object AddEditPage: NavRoutes("addEdit_page")
}
