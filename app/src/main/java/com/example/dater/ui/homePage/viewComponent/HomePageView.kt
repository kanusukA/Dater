package com.example.dater.ui.homePage.viewComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dater.ui.components.JourneyBox.JourneyBoxViewModel
import com.example.dater.ui.components.JourneyBox.viewComponent.JourneyBox
import com.example.dater.ui.components.ReminderBox.viewComponent.ReminderBox
import com.example.dater.ui.components.TopFilterBar.TopFilterBarType
import com.example.dater.ui.homePage.HomePageEvents
import com.example.dater.ui.homePage.HomePageViewModel

@ExperimentalMaterial3Api
@Composable
fun HomePageView(
    modifier: Modifier = Modifier,
    viewModel: HomePageViewModel
) {
    // Not reflecting changes
    val journeys by viewModel.journeys.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()
    val journeyWidgetSelection = viewModel.journeyWidgetSelection.collectAsState().value

    val topBarState = viewModel.homePageState.collectAsState().value

    Column(
        modifier = modifier
    ) {
        Box {
            LazyColumn {
                when (topBarState) {

                    is TopFilterBarType.JourneyType -> {

                        items(
                            count = journeys.size,
                            key = { index -> journeys[index].id },
                            itemContent = { index ->

                                val journey = journeys[index]

                                val journeyReminders = viewModel.getReminders(journey)


                                val journeyViewModel = remember {
                                    mutableStateOf(
                                        JourneyBoxViewModel(
                                            journey = journey,
                                            notification = journey.notification,
                                            reminders = journeyReminders,
                                            deleteJourney = {
                                                viewModel.onEvent(
                                                    HomePageEvents.DeleteJourney(
                                                        journey = journey,
                                                        listOfReminders = it
                                                    )
                                                )
                                            },
                                            onChangeJourneyNotificationState = {
                                                viewModel.onEvent(HomePageEvents.EditJourney(it))
                                            }
                                        )
                                    )
                                }

                                JourneyBox(
                                    viewModel = journeyViewModel.value,
                                    deleteReminder = {
                                        viewModel.onEvent(
                                            HomePageEvents.DeleteReminder(
                                                it
                                            )
                                        )
                                    },
                                    journeyWidgetSelection = journeyWidgetSelection,
                                    onChangeJourneyWidgetType = {
                                        viewModel.onEvent(
                                            HomePageEvents.ChangeJourneyWidgetType(
                                                journeyWidgetType = it,
                                                journey = journey
                                            )
                                        )
                                    },
                                    forceToPrimary = {
                                        viewModel.onEvent(
                                            HomePageEvents.ForceJourneyWidgetToPrimary(
                                                journey
                                            )
                                        )
                                    },
                                    onChangeReminder = {
                                        viewModel.onEvent(
                                            HomePageEvents.UpdateReminder(
                                                it
                                            )
                                        )
                                    }
                                )

                            }
                        )
                    }

                    is TopFilterBarType.ReminderType -> {

                       items(

                           count = reminders.size,
                           key = {index -> reminders[index].id},
                           itemContent = {index ->
                               val reminder = reminders[index]

                               ReminderBox(
                                   modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                                   reminder = reminder,
                                   editable = true,
                                   expandable = true,
                                   initialEditState = false,
                                   initialExpandState = false,
                                   onSaveReminder = {
                                       viewModel.onEvent(
                                           HomePageEvents.UpdateReminder(
                                               it
                                           )
                                       )
                                   },
                                   onDeleteReminder = {
                                       viewModel.onEvent(
                                           HomePageEvents.DeleteReminder(
                                               reminder
                                           )
                                       )
                                   }
                               )

                           }
                       )

                    }

                }
            }

        }

    }

}

