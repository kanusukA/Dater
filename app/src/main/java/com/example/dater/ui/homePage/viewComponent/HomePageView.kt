package com.example.dater.ui.homePage.viewComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.dater.ui.components.JourneyBox.JourneyBoxViewModel
import com.example.dater.ui.components.JourneyBox.viewComponent.JourneyBox
import com.example.dater.ui.components.ReminderBox.viewComponent.ReminderBoxView
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import com.example.dater.ui.homePage.HomePageEvents
import com.example.dater.ui.homePage.HomePageViewModel

@ExperimentalMaterial3Api
@Composable
fun HomePageView(
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel
) {

    val journeys = viewModel.journeys.collectAsState(emptyList()).value
    val reminders = viewModel.reminders.collectAsState(emptyList()).value
    val topBarState = viewModel.topFilterBarState.collectAsState().value


    Column(
        modifier = modifier
    ) {
        Box {
            LazyColumn {
                when (topBarState) {

                    is TopFilterBarState.JourneyState -> {

                        items(journeys) {journey ->
                            JourneyBox(
                                viewModel = JourneyBoxViewModel(
                                journey = journey,
                                reminders = viewModel.getReminders(journey).collectAsState().value,
                                deleteJourney = {
                                    viewModel.onEvent(
                                        HomePageEvents.DeleteJourney(
                                            journey = journey,
                                            listOfReminders = it
                                        )
                                    )
                                }),
                                deleteReminder = {viewModel.onEvent(HomePageEvents.DeleteReminder(it))},
                                onChangeReminder = {viewModel.onEvent(HomePageEvents.UpdateReminder(it))}
                            )
                        }
                    }

                    is TopFilterBarState.ReminderState -> {

                        items(reminders) { reminder ->

                            ReminderBoxView(
                                reminder = reminder,
                                editable = true,
                                expandable = true,
                                initialEditState = false,
                                initialExpandState = false,
                                onSaveReminder = {viewModel.onEvent(HomePageEvents.UpdateReminder(it))},
                                onDeleteReminder = {viewModel.onEvent(HomePageEvents.DeleteReminder(reminder))}
                            )

                        }

                    }

                }
            }

        }

    }

}

