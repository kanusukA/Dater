package com.example.dater.ui.components.JourneyBox.viewComponent


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dater.Data.Journey.domain.model.JourneyWidgetSelection
import com.example.dater.Data.Journey.utils.JourneyWidgetType
import com.example.dater.Data.Journey.utils.getJourneyWidgetType
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.Reminder.utils.ReminderTypeIconView
import com.example.dater.R
import com.example.dater.ui.components.JourneyBox.JourneyBoxEvents
import com.example.dater.ui.components.JourneyBox.JourneyBoxViewModel
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderAlertAnimation
import com.example.dater.ui.components.ReminderBox.viewComponent.ReminderBox
import com.example.dater.ui.components.SelectionBox.RowSelectionBox
import kotlinx.coroutines.delay

// Add Journey Selection Type in Journey Box
@ExperimentalMaterial3Api
@Composable
fun JourneyBox(
    viewModel: JourneyBoxViewModel,
    journeyWidgetSelection: JourneyWidgetSelection,
    onChangeJourneyWidgetType: (JourneyWidgetType) -> Unit,
    forceToPrimary: () -> Unit,
    onChangeReminder: (Reminder) -> Unit,
    deleteReminder: (Reminder) -> Unit
) {

    val listOfReminders by viewModel.listReminders.collectAsStateWithLifecycle()
    val alertState by viewModel.alertState.collectAsStateWithLifecycle()
    val title = viewModel.title.collectAsState().value
    val startDate = viewModel.startDateText.collectAsState().value
    val endDate = viewModel.endDateText.collectAsState().value
    val timeLeft = viewModel.timeLeft.collectAsState().value
    val expand = viewModel.expand.collectAsState().value
    val selectedReminderIndex = viewModel.selectedReminderIndex.collectAsState().value
    val endDateLong = viewModel.journey.endDate


//    val notification = JourneyNotifierRequest(context, viewModel.journey.id)

    val journeySelected = remember(key1 = journeyWidgetSelection) {
        mutableStateOf(
            getJourneyWidgetType(journeyWidgetSelection, viewModel.journey)
        )
    }

    JourneyBoxView(
        title = title,
        timeLeft = timeLeft,
        startDate = startDate,
        endDate = endDate,
        endDateLong = endDateLong,
        expand = expand,
        listOfReminders = listOfReminders,
        selectedReminderIndex = selectedReminderIndex,
        journeySelected = journeySelected.value,
        alertState = alertState,
        onSelectReminderType = {
            viewModel.onEvent(
                JourneyBoxEvents.SelectReminderType(
                    it
                )
            )
        },
        onExpand = { viewModel.onEvent(JourneyBoxEvents.Expand) },
        onSaveReminder = { onChangeReminder(it) },
        onDeleteReminder = { deleteReminder(it) },
        onChangeWidgetType = { onChangeJourneyWidgetType(it) },
        forceToPrimary = { forceToPrimary() },
        onDeleteJourney = { viewModel.onEvent(JourneyBoxEvents.DeleteJourney) },
        onChangeAlertState = { viewModel.onEvent(JourneyBoxEvents.AlertState) }
    )


}

@Composable
private fun JourneyBoxView(
    title: String,
    timeLeft: Long,
    startDate: String,
    endDate: String,
    endDateLong: Long,
    expand: Boolean,
    listOfReminders: List<Reminder>,
    selectedReminderIndex: Int,
    journeySelected: JourneyWidgetType,
    alertState: Boolean,
    onSelectReminderType: (ReminderType) -> Unit,
    onSaveReminder: (Reminder) -> Unit,
    onDeleteReminder: (Reminder) -> Unit,
    onChangeWidgetType: (JourneyWidgetType) -> Unit,
    onChangeAlertState: () -> Unit,
    forceToPrimary: () -> Unit,
    onExpand: () -> Unit,
    onDeleteJourney: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 6.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(20.dp),
        onClick = { onExpand() },
    ) {

        JourneyStars(journeySelected = journeySelected)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.rubik_font)),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = timeLeft.toString(),
                fontFamily = FontFamily(Font(R.font.rubik_font)),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(120.dp, 45.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {


                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Black,

                                    )
                            ) {
                                append(startDate.subSequence(0, 2))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                )
                            ) {
                                append(startDate.subSequence(2, 5))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Black,
                                )
                            ) {
                                append(startDate.subSequence(5, 9))
                            }
                        },
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontFamily = FontFamily(Font(R.font.rubik_font)),
                        fontSize = 18.sp
                    )
                }

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

                Box(
                    modifier = Modifier
                        .size(120.dp, 45.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Black,

                                    )
                            ) {
                                append(endDate.subSequence(0, 2))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                )
                            ) {
                                append(endDate.subSequence(2, 5))
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Black,
                                )
                            ) {
                                append(endDate.subSequence(5, 9))
                            }
                        },
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontFamily = FontFamily(Font(R.font.rubik_font)),
                        fontSize = 18.sp
                    )
                }

            }


            if (expand) {

                RowSelectionBox(
                    columnModifier = Modifier.padding(horizontal = 6.dp),
                    boxModifier = Modifier.requiredHeightIn(max = 300.dp),
                    buttonsPadding = PaddingValues(top = 12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    selectedIndex = selectedReminderIndex,
                    showIndent = true,
                    borderStroke = BorderStroke(0.dp, color = Color.Transparent),
                    maxButtonSize = 26.dp,
                    buttons = {
                        ReminderTypeIconView(
                            showAll = true,
                            selectedReminderType = selectedReminderIndex,
                            selectedReminderColor = MaterialTheme.colorScheme.tertiary,
                            onSelectReminderType = { onSelectReminderType(it) }
                        )
                    }
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
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

                                items(
                                    count = listOfReminders.size,
                                    key = { index -> listOfReminders[index].id },
                                    itemContent = { index ->

                                        val reminder = listOfReminders[index]

                                        ReminderBox(
                                            reminder = reminder,
                                            editable = true,
                                            expandable = true,
                                            initialEditState = false,
                                            initialExpandState = false,
                                            journeyEndDate = endDateLong,
                                            onSaveReminder = { onSaveReminder(reminder) },
                                            onDeleteReminder = { onDeleteReminder(reminder) }
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))

                                    }
                                )

                            }
                        } else {
                            Text(
                                text = "No Reminders",
                                fontWeight = FontWeight.Black,
                                fontSize = 26.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(vertical = 12.dp)
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            AnimatedVisibility(visible = expand, modifier = Modifier.align(Alignment.End)) {

                Row(
                    modifier = Modifier
                        .padding(end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    journeyStarButton(
                        journeySelected = journeySelected,
                        onClickJourneyWidgetType = { onChangeWidgetType(it) },
                        forceToPrimary = { forceToPrimary() }
                    )


                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                            color = if (alertState) {
                                MaterialTheme.colorScheme.tertiaryContainer
                            } else {
                                Color.Transparent
                            },
                            shape = CircleShape
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ReminderAlertAnimation(modifier = Modifier.size(24.dp)) {
                            onChangeAlertState()
                        }
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.debug_info_icon),
                            contentDescription = "notification info"
                        )
                    }

                    TextButton(onClick = {/*TODO EDIT JOURNEY*/ }) {
                        Text(text = "Edit")
                    }
                    Button(onClick = { onDeleteJourney() }) {
                        Text(text = "Delete")
                    }
                }
            }

        }
    }
}

@Composable
private fun JourneyStars(
    journeySelected: JourneyWidgetType
) {
    AnimatedVisibility(
        visible = journeySelected != JourneyWidgetType.None,

        ) {
        when (journeySelected) {
            JourneyWidgetType.None -> { /* Do Nothing */
            }

            JourneyWidgetType.PrimarySelection -> {
                Image(
                    modifier = Modifier
                        .size(110.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.journey_star_edge),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    alpha = 0.15f,
                    contentDescription = ""
                )
            }

            JourneyWidgetType.SecondarySelection -> {
                Image(
                    modifier = Modifier
                        .size(110.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.journey_star_edge),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                    alpha = 0.15f,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun journeyStarButton(
    journeySelected: JourneyWidgetType,
    onClickJourneyWidgetType: (JourneyWidgetType) -> Unit,
    forceToPrimary: () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed = interactionSource.collectIsPressedAsState().value

    // Forced To Primary
    LaunchedEffect(key1 = isPressed) {
        while (isPressed) {
            delay(1200)
            // on Hold
            forceToPrimary()
        }

    }
    when (journeySelected) {
        JourneyWidgetType.None -> {

            IconButton(
                onClick = { onClickJourneyWidgetType(JourneyWidgetType.None) },
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.journey_star_edge),
                    tint = Color.Black,
                    contentDescription = ""
                )
            }

        }

        JourneyWidgetType.PrimarySelection -> {

            IconButton(
                onClick = { onClickJourneyWidgetType(JourneyWidgetType.PrimarySelection) },
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.journey_star_edge),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        JourneyWidgetType.SecondarySelection -> {

            IconButton(
                onClick = { onClickJourneyWidgetType(JourneyWidgetType.SecondarySelection) },
                interactionSource = interactionSource
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.journey_star_edge),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
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

//@Preview
//@Composable
//fun previewJourneyBox(){
//
//    val context = LocalContext.current
//
//    JourneyBoxView(
//        title = "This is a Journey Box",
//        timeLeft = 365L,
//        startDate = "27FEB2004",
//        endDate = "27FEB2004",
//        expand = true,
//        listOfReminders = emptyList(),
//        selectedReminderIndex = 0,
//        journeySelected = JourneyWidgetType.None,
//        notification = JourneyNotifierRequest(context,0),
//        onSelectReminderType = {},
//        onSaveReminder = {},
//        onDeleteReminder = {},
//        onChangeWidgetType = {},
//        forceToPrimary = { /*TODO*/ },
//        onExpand = { /*TODO*/ },
//        onDeleteJourney = {}
//    )
//}