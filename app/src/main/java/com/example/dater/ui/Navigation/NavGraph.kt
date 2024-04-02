@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dater.ui.Navigation

import androidx.compose.material3.BottomAppBarState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dater.ui.addEditPage.AddEditViewModel
import com.example.dater.ui.addEditPage.viewComponent.AddEditView
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.BottomNavBar.BottomNavBarViewModel
import com.example.dater.ui.homePage.HomePageViewModel
import com.example.dater.ui.homePage.viewComponent.HomePageView

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val addEditViewModel = hiltViewModel<AddEditViewModel>()
    val homePageViewModel = hiltViewModel<HomePageViewModel>()

    val viewModel = hiltViewModel<NavStateViewModel>()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HomePage.routes,
        modifier = modifier
    ) {

        composable(route = NavRoutes.HomePage.routes) {
            LaunchedEffect(key1 = true){
                viewModel.changeNavState(NavRoutes.HomePage)
            }
            HomePageView(viewModel = homePageViewModel)
        }

        composable(route = NavRoutes.AddEditPage.routes) {
            LaunchedEffect(key1 = true) {
                viewModel.changeNavState(NavRoutes.AddEditPage)
                addEditViewModel.clear()
            }
            AddEditView(
                viewModel = addEditViewModel,
            )
        }
    }
}