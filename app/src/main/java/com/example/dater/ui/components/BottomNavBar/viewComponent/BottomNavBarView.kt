package com.example.dater.ui.components.BottomNavBar.viewComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dater.ui.Navigation.NavRoutes
import com.example.dater.ui.addEditPage.AddEditBottomNavBarEvents
import com.example.dater.ui.addEditPage.AddEditViewModel
import com.example.dater.ui.components.BottomNavBar.BottomNavBarEvents
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.BottomNavBar.BottomNavBarViewModel

@Composable
fun BottomNavBarView(
    modifier: Modifier = Modifier,
    onClickSave:() -> Unit = {},
    onClickCancel: () -> Unit = {},
    navController: NavController
) {
    val viewModel = hiltViewModel<BottomNavBarViewModel>()
    val state: BottomNavBarState = viewModel.state.collectAsState().value

    LaunchedEffect(state){
        when(state){
            BottomNavBarState.AddEditPage -> {
                navController.navigate(NavRoutes.AddEditPage.routes)
            }
            BottomNavBarState.HomePage -> {
                navController.navigate(NavRoutes.HomePage.routes)
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(60.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        when (state) {
            BottomNavBarState.AddEditPage -> {

                FilledTonalButton(onClick = {
                    // ADD EDIT PAGE - CANCEL
                    onClickCancel()
                }) {
                    Text(text = "Cancel")
                }

                FilledTonalButton(onClick = {
                    // ADD EDIT PAGE - SAVE
                    onClickSave()
                }) {
                    Text(text = "Okay")
                }
            }

            BottomNavBarState.HomePage -> {
                FilledTonalButton(
                    onClick = {
                        viewModel.bottomNavBarEvents(BottomNavBarEvents.CreateJourney)
                    }
                ) {
                    Text(text = "Create Journey")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
//    BottomNavBarView()
}