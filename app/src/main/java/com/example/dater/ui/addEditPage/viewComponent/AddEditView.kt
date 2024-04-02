@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dater.ui.addEditPage.viewComponent


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults


import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.R
import com.example.dater.ui.addEditPage.AddEditEvents
import com.example.dater.ui.addEditPage.AddEditViewModel
import com.example.dater.ui.addEditPage.AddEditViewState
import com.example.dater.ui.addEditPage.DateType
import com.example.dater.ui.components.MyDatePicker.SimpleDatePicker
import com.example.dater.ui.components.ReminderBox.viewComponent.ReminderBox


@Composable
fun AddEditView(
    viewModel: AddEditViewModel
) {

    val reminderList = viewModel.reminderList.collectAsState().value
    val datePickerState = rememberDatePickerState()
    val showDatePicker = viewModel.showDatePicker.collectAsState().value
    var datePicker by remember {
        mutableStateOf<DateType>(DateType.StartDate(AddEditViewState.JOURNEY))
    }

    val journey = viewModel.journey.collectAsState().value
    val reminder = viewModel.reminder.collectAsState().value
    val addEditViewState = viewModel.addEditViewState.collectAsState().value

    val reminderStartDateEmpty = viewModel.reminderStartDateEmpty.collectAsState().value
    val reminderEndDateEmpty = viewModel.reminderEndDateEmpty.collectAsState().value

    //String
    val startDateText = viewModel.startDateText.collectAsState().value
    val endDateText = viewModel.endDateText.collectAsState().value
    val reminderStartDateText = viewModel.reminderStartDateText.collectAsState().value
    val reminderEndDateText = viewModel.reminderEndDateText.collectAsState().value

    //Errors
    val titleErrors = viewModel.titleErrors.collectAsState().value

    // DATE PICKER -----------------------------------------------------------------------------
    if (showDatePicker) {
        SimpleDatePicker(
            datePickerState = datePickerState,
            show = showDatePicker,
            dismiss = { viewModel.onEvent(AddEditEvents.showDatePicker(false)) },
            setDate = {
                viewModel.onEvent(
                    AddEditEvents.SetDatePicker(datePicker, it)
                )
            }
        )
    }
    // DATE PICKER -----------------------------------------------------------------------------


    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        when (addEditViewState) {
            AddEditViewState.JOURNEY -> {

                AddEditJourneyView(
                    journey = journey,
                    startDateText = startDateText,
                    endDateText = endDateText,
                    reminderList = reminderList,
                    onClickStartDate = {
                        viewModel.onEvent(AddEditEvents.showDatePicker(true))
                        datePicker = DateType.StartDate(AddEditViewState.JOURNEY)
                    },
                    onClickEndDate = {
                        viewModel.onEvent(AddEditEvents.showDatePicker(true))
                        datePicker = DateType.EndDate(AddEditViewState.JOURNEY)
                    },
                    onChangeTitle = { viewModel.onEvent(AddEditEvents.ChangeTitle(it)) },
                    onAddReminder = { viewModel.onEvent(AddEditEvents.AddReminder) },
                    onUpdateReminder = { index, reminder ->
                        viewModel.onEvent(
                            AddEditEvents.ChangeReminderInList(index, reminder)
                        )
                    },
                    onDeleteReminder = { viewModel.onEvent(AddEditEvents.DeleteReminderInList(it)) },
                    titleErrorEmpty = titleErrors.titleEmpty,
                    titleErrorLong = titleErrors.titleLong,
                )

            }

            AddEditViewState.REMINDER -> {

                AddEditReminderView(
                    reminder = reminder,
                    startDateText = reminderStartDateText,
                    endDateText = reminderEndDateText,
                    startDateEmpty = reminderStartDateEmpty,
                    endDateEmpty = reminderEndDateEmpty,
                    onClickStartDate = {
                        viewModel.onEvent(AddEditEvents.showDatePicker(true))
                        datePicker = DateType.StartDate(AddEditViewState.REMINDER)
                    },
                    onClickEndDate = {
                        viewModel.onEvent(AddEditEvents.showDatePicker(true))
                        datePicker = DateType.EndDate(AddEditViewState.REMINDER)
                    },
                    onChangeReminder = { viewModel.onEvent(AddEditEvents.ChangeReminder(it)) }
                )
            }
        }

    }


}

@Composable
private fun AddEditJourneyView(
    journey: Journey,
    startDateText: String,
    endDateText: String,
    reminderList: List<Reminder>,
    onClickStartDate: () -> Unit,
    onClickEndDate: () -> Unit,
    onChangeTitle: (String) -> Unit,
    onAddReminder: () -> Unit,
    onUpdateReminder: (Int, Reminder) -> Unit,
    onDeleteReminder: (Int) -> Unit,
    titleErrorEmpty: Boolean = false,
    titleErrorLong: Boolean = false
) {


    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            DateButton(
                onClick = { onClickStartDate() },
                text = startDateText,
            )

            DateButton(
                onClick = { onClickEndDate() },
                text = endDateText,
            )
        }

        OutlinedTextField(
            value = journey.title,
            onValueChange = { onChangeTitle(it) },
            label = { Text(text = "Title") },
            isError = titleErrorEmpty || titleErrorLong,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
                .shadow(4.dp, shape = RoundedCornerShape(15.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(15.dp)
                )


        ) {
            FloatingActionButton(
                onClick = { onAddReminder() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            ) {
                Text(text = "+")
            }



            if (reminderList.isEmpty()) {
                Text(
                    text = "Insert Reminder",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {

                    itemsIndexed(reminderList) { index, item ->

                        ReminderBox(
                            reminder = item,
                            editable = true,
                            expandable = true,
                            initialEditState = true,
                            initialExpandState = true,
                            onSaveReminder = { onUpdateReminder(index, it) },
                            onDeleteReminder = { onDeleteReminder(index) },
                            journeyEndDate = journey.endDate
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))

                    }

                }

            }

        }

    }
}

@Composable
private fun AddEditReminderView(
    modifier: Modifier = Modifier,
    reminder: Reminder,
    startDateText: String,
    endDateText: String,
    startDateEmpty: Boolean,
    endDateEmpty: Boolean,
    onChangeReminder: (Reminder) -> Unit,
    onClickStartDate: () -> Unit,
    onClickEndDate: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeightIn(300.dp, 400.dp)
            .padding(6.dp)
            .shadow(2.dp, shape = RoundedCornerShape(15.dp))
            .background(
                shape = RoundedCornerShape(15.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {

            DateButton(
                onClick = { onClickStartDate() },
                text = startDateText,
            )
            AnimatedVisibility(visible = !startDateEmpty) {
                DateButton(
                    onClick = { onClickEndDate() },
                    text = endDateText
                )
            }

        }
        OutlinedTextField(
            value = reminder.title,
            onValueChange = { onChangeReminder(reminder.copy(title = (it))) },
            label = { Text(text = "Title") }
        )
        OutlinedTextField(
            value = reminder.description,
            onValueChange = { onChangeReminder(reminder.copy(description = it)) },
            label = { Text(text = "Description") }
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            SegmentedButton(
                selected = reminder.reminderType == ReminderType.Event,
                onClick = { onChangeReminder(reminder.copy(reminderType = ReminderType.Event)) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reminder_events_icon),
                        contentDescription = "Event"
                    )
                }
            ) {
                Text(text = "Event")
            }

            SegmentedButton(
                selected = reminder.reminderType == ReminderType.Alert,
                onClick = { onChangeReminder(reminder.copy(reminderType = ReminderType.Alert)) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reminder_alert_icon),
                        contentDescription = "Alert"
                    )
                }
            ) {
                Text(text = "Alert")
            }

            SegmentedButton(
                selected = reminder.reminderType == ReminderType.Birthday,
                onClick = { onChangeReminder(reminder.copy(reminderType = ReminderType.Birthday)) },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.reminder_birthday_icon),
                        contentDescription = "Birthday"
                    )
                }
            ) {
                Text(text = "Birthday")
            }
        }

    }

}


@Composable
fun DateButton(
    onClick: () -> Unit,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {

    /*TODO ADD CUSTOM ANIMATIONS*/

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.outlinedButtonColors(),
        shape = CircleShape,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
    }
}


@Preview
@Composable
fun PreviewAddEditView() {

}