package com.example.dater.ui.components.BottomNavBar.viewComponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dater.R
import com.example.dater.devTools.DevTools
import com.example.dater.ui.Navigation.NavRoutes
import com.example.dater.ui.addEditPage.AddEditBottomNavBarEvents
import com.example.dater.ui.addEditPage.AddEditViewModel
import com.example.dater.ui.components.BottomNavBar.BottomNavBarState
import com.example.dater.ui.components.BottomNavBar.BottomNavBarViewModel

@Composable
fun BottomNavBarView(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val addEditViewModel = hiltViewModel<AddEditViewModel>()
    val viewModel = hiltViewModel<BottomNavBarViewModel>()

    val state = viewModel.bottomNavBarState.collectAsState().value
    val showDevTab = viewModel.devTabShow.collectAsState().value

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            ),


    ) {

        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = { viewModel.devTab(true) }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.bug_icon),
                contentDescription = "debugging"
            )
        }


        AnimatedContent(
            modifier = Modifier
                .align(Alignment.Center)
            ,
            targetState = showDevTab,
            label = ""
        ) {show ->
            Column(
                modifier = Modifier.widthIn(max = 300.dp)
            ){
                JourneyButtons(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    showDevTab = show,
                    state = state,
                    onCloseDevTab = { viewModel.devTab(false) },
                    onOpenDevTab = {viewModel.devTab(true)},
                    onCancel = {
                        addEditViewModel.onBottomNavBarEvents(
                            AddEditBottomNavBarEvents.Cancel,
                            navController = navController
                        )
                    },
                    onSave = {
                        addEditViewModel.onBottomNavBarEvents(
                            AddEditBottomNavBarEvents.Save,
                            navController = navController
                        )
                    },
                    onCreateJourney = {
                        navController.navigate(NavRoutes.AddEditPage.routes)
                    }
                )

                AnimatedVisibility(visible = show) {
                    DevTools()
                }

            }

        }

    }
}

@Composable
private fun JourneyButtons(
    modifier: Modifier  = Modifier,
    state: BottomNavBarState,
    showDevTab: Boolean,
    onOpenDevTab: () -> Unit,
    onCloseDevTab: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onCreateJourney: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){

        if (showDevTab) {
            FilledTonalButton(onClick = {
                onCloseDevTab()
            }) {
                Text(text = "Close")
            }
        } else {
            when (state) {

                BottomNavBarState.AddEditPage -> {

                    FilledTonalButton(onClick = {
                        onCancel()
                    }) {
                        Text(text = "Cancel")
                    }

                    FilledTonalButton(onClick = {
                        onSave()
                    }) {
                        Text(text = "Okay")
                    }

                }

                BottomNavBarState.HomePage -> {
                    FilledTonalButton(
                        onClick = {
                            onCreateJourney()
                        }
                    ) {
                        Text(text = "Create Journey")
                    }
                }
            }
        }
    }

}



