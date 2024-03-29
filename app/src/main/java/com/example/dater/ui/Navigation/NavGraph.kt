@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dater.ui.Navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dater.ui.addEditPage.AddEditViewModel
import com.example.dater.ui.addEditPage.viewComponent.AddEditView
import com.example.dater.ui.homePage.HomePageViewModel
import com.example.dater.ui.homePage.viewComponent.HomePageView

@Composable
fun NavGraph(
            navController: NavHostController,
             modifier: Modifier = Modifier
){

    val addEditViewModel = hiltViewModel<AddEditViewModel>()
    val homePageViewModel = hiltViewModel<HomePageViewModel>()

    NavHost(navController = navController,
        startDestination = NavRoutes.HomePage.routes,
        modifier = modifier
        ){

        composable(route = NavRoutes.HomePage.routes){

            HomePageView(viewModel = homePageViewModel)
        }

        composable(route = NavRoutes.AddEditPage.routes){
            LaunchedEffect(key1 = true){
                addEditViewModel.clear()
            }
            AddEditView(addEditViewModel)
        }
    }
}