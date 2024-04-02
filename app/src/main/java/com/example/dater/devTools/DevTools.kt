package com.example.dater.devTools


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.model.ReminderDateType
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.utils.DateHandler
import com.example.dater.alarms.reminderAlarm.ReminderAlarmItem
import com.example.dater.alarms.reminderAlarm.ReminderSchedulerImpl

@Composable
fun DevTools(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<DevToolsViewModel>()
    val journeys = viewModel.journeys.collectAsState().value
    val reminders = viewModel.reminders.collectAsState().value
    val state = viewModel.selectedState.collectAsState().value
    val workCount = viewModel.workStatus.collectAsState().value

    // Make a debuging ui to test this

    var showMenu by remember {
        mutableStateOf(false)
    }

    reminderAlarmTest()

}

@Composable
private fun reminderAlarmTest() {

    val context = LocalContext.current
    val reminderAlarmScheduler = ReminderSchedulerImpl(context)

    val reminderSingle by remember {
        mutableStateOf(
            Reminder(
                startDate = DateHandler().apply { addTO(sec = 20) }.getLong(),
                endDate = 0L,
                "Alert Single Date Reminder",
                "alert single date reminder description",
                ReminderType.Alert,
                id = 1
            )
        )
    }
    val reminderDouble by remember {
        mutableStateOf(
            Reminder(
                startDate = DateHandler().apply { addTO(sec = 20) }.getLong(),
                endDate = DateHandler().apply { addTO(sec = 40) }.getLong(),
                "Alert Double Date Reminder",
                "alert Double date reminder description",
                ReminderType.Alert,
                id = 2
            )
        )
    }

    val reminderBirthday by remember {
        mutableStateOf(
            Reminder(
                startDate = DateHandler().apply { addTO(sec = 20) }.getLong(),
                endDate = 0L,
                "Birthday Date Reminder",
                "birthday date reminder description",
                ReminderType.Birthday,
                id = 3
            )
        )
    }

    val reminderEveryDay by remember {
        mutableStateOf(
            Reminder(
                startDate = DateHandler().getLong(),
                endDate = DateHandler().apply { addTO(date = 2) }.getLong(),
                "EveryDay Date Reminder",
                "EveryDay date reminder description",
                ReminderType.Event,
                ReminderDateType.EmptyDate,
                id = 4
            )
        )
    }

    val reminderTimeFrame by remember {
        mutableStateOf(
            Reminder(
                startDate = DateHandler().apply { addTO(date = 1) }.getLong(),
                endDate = DateHandler().apply { addTO(date = 3) }.getLong(),
                "TimeFrame Date Reminder",
                "TimeFrame date reminder description",
                ReminderType.Event,
                ReminderDateType.SelectedDate,
                id = 5
            )
        )
    }
    Column {

        Row {
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.schedule(
                    alarmItem = ReminderAlarmItem(
                        reminderSingle,
                        reminderSingle.id.toLong()
                    )
                )
            }) {
                Text(text = "Reminder Alert Single Date")
            }
            Spacer(modifier = Modifier.width(14.dp))
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.cancel(reminderSingle.reminderType,reminderSingle.id.toLong())
            }) {
                Text(text = "Cancel")
            }
        }

        Row {
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.schedule(alarmItem = ReminderAlarmItem(
                    reminderDouble,
                    reminderDouble.id.toLong()
                ))
            }) {
                Text(text = "Reminder Alert Double Date")
            }
            Spacer(modifier = Modifier.width(14.dp))
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.cancel(reminderDouble.reminderType,reminderDouble.id.toLong())
            }) {
                Text(text = "Cancel")
            }
        }

        Row {
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.schedule(ReminderAlarmItem(
                    reminderBirthday,
                    reminderBirthday.id.toLong()
                ))
            }) {
                Text(text = "Reminder Birthday Date")
            }
            Spacer(modifier = Modifier.width(14.dp))
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.cancel(reminderBirthday.reminderType,reminderBirthday.id.toLong())
            }) {
                Text(text = "Cancel")
            }
        }

        Row {
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.schedule(ReminderAlarmItem(
                    reminderEveryDay,
                    reminderEveryDay.id.toLong()
                ))
            }) {
                Text(text = "Reminder Event Every Date")
            }
            Spacer(modifier = Modifier.width(14.dp))
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.cancel(reminderEveryDay.reminderType,reminderEveryDay.id.toLong())
            }) {
                Text(text = "Cancel")
            }
        }

        Row {
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.schedule(ReminderAlarmItem(
                    reminderTimeFrame,
                    reminderTimeFrame.id.toLong()
                ))
            }) {
                Text(text = "Reminder Event TimeFrame Date")
            }
            Spacer(modifier = Modifier.width(14.dp))
            FilledTonalButton(onClick = {
                reminderAlarmScheduler.cancel(reminderTimeFrame.reminderType,reminderTimeFrame.id.toLong())
            }) {
                Text(text = "Cancel")
            }
        }

    }
}