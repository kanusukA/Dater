package com.example.dater.ui.components.JourneyBox.viewComponent


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.Reminder.utils.ReminderTypeIconView
import com.example.dater.R
import com.example.dater.ui.addEditPage.viewComponent.DateButton
import com.example.dater.ui.components.JourneyBox.JourneyBoxEvents
import com.example.dater.ui.components.JourneyBox.JourneyBoxViewModel
import com.example.dater.ui.components.ReminderBox.viewComponent.ReminderBoxView
import com.example.dater.ui.components.SelectionBox.RowSelectionBox

@ExperimentalMaterial3Api
@Composable
fun JourneyBox(
    viewModel: JourneyBoxViewModel,
    onChangeReminder: (Reminder) -> Unit,
    deleteReminder: (Reminder) -> Unit
) {

    val listOfReminders = viewModel.listReminders.collectAsState().value
    val title = viewModel.title.collectAsState().value
    val startDate = viewModel.startDateText.collectAsState().value
    val endDate = viewModel.endDateText.collectAsState().value
    val timeLeft = viewModel.timeLeft.collectAsState().value
    val expand = viewModel.expand.collectAsState().value
    val selectedReminderIndex = viewModel.selectedReminderIndex.collectAsState().value

    Surface(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(20.dp),
        onClick = { viewModel.onEvent(JourneyBoxEvents.Expand) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Text(
                text = timeLeft.toString(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                DateButton(
                    onClick = { /*TODO*/ },
                    text = startDate
                )

                AnimatedVisibility(visible = !expand) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        ReminderDots(
                            modifier = Modifier.padding(2.dp),
                            remindersList = listOfReminders
                        )
                    }
                }

                DateButton(
                    onClick = { /*TODO*/ },
                    text = endDate,
                )

            }


            if (expand) {

                RowSelectionBox(
                    modifier = Modifier.requiredHeightIn(max = 300.dp),
                    buttonsPadding = PaddingValues(bottom = 6.dp, top = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    selectedIndex = selectedReminderIndex,
                    showIndent = true,
                    buttons = {
                        ReminderTypeIconView(
                            showAll = true,
                            selectedReminderType = selectedReminderIndex,
                            selectedReminderColor = MaterialTheme.colorScheme.tertiary,
                            onSelectReminderType = {
                                viewModel.onEvent(
                                    JourneyBoxEvents.SelectReminderType(
                                        it
                                    )
                                )
                            }
                        )
                    }
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        if (listOfReminders.isNotEmpty()) {
                            LazyColumn(
                                contentPadding = PaddingValues(
                                    horizontal = 4.dp,
                                    vertical = 4.dp
                                )
                            ) {

                                items(listOfReminders) { reminder ->

                                    ReminderBoxView(
                                        reminder = reminder,
                                        editable = true,
                                        expandable = true,
                                        initialEditState = false,
                                        initialExpandState = false,
                                        onSaveReminder = { onChangeReminder(reminder) },
                                        onDeleteReminder = { deleteReminder(reminder) }
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "No Reminders",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(vertical = 12.dp)
                            )
                        }
                    }

                }
            }

            AnimatedVisibility(visible = expand, modifier = Modifier.align(Alignment.End)) {
                Row(
                    modifier = Modifier
                        .padding(end = 4.dp)
                ) {
                    TextButton(onClick = {/*TODO EDIT JOURNEY*/ }) {
                        Text(text = "Edit")
                    }
                    Button(onClick = { viewModel.onEvent(JourneyBoxEvents.DeleteJourney) }) {
                        Text(text = "Delete")
                    }
                }
            }

        }
    }
}

@Composable
fun ReminderDots(
    modifier: Modifier = Modifier,
    remindersList: List<Reminder>,

    ) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.reminder_alert_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = "${remindersList.count { it.reminderType == ReminderType.Alert }}",
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }


        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.reminder_events_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = "${remindersList.count { it.reminderType == ReminderType.Event }}",
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }


        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.reminder_birthday_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = "${remindersList.count { it.reminderType == ReminderType.Birthday }}",
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }


    }
}


@Preview
@Composable
fun PreviewJourneyBox() {

}