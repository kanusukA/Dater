@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.example.dater.ui.components.ReminderBox.viewComponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.model.ReminderDateType
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.utils.DateHandler
import com.example.dater.R
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderAlertAnimation
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderBirthdayAnimation
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderEventAnimation
import com.example.dater.ui.components.SelectionBox.ColumnSelectionBox
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionAlignment
import com.example.dater.ui.daterTextBox.DaterTextField

@Composable
fun ReminderBox(
    modifier: Modifier = Modifier,
    reminder: Reminder,
    editable: Boolean,
    expandable: Boolean,
    initialEditState: Boolean,
    initialExpandState: Boolean,
    onSaveReminder: (Reminder) -> Unit,
    onDeleteReminder: () -> Unit,
    journeyEndDate: Long = 0L
) {
    var savableReminder by remember {
        mutableStateOf(reminder)
    }

    var editState by remember {
        mutableStateOf(initialEditState)
    }

    var expandState by remember {
        mutableStateOf(initialExpandState)
    }

    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        ReminderBoxView(
            reminder = savableReminder,
            editState = editState,
            expandState = expandState,
            journeyEndDate = journeyEndDate,
            onChangeExpandState = {
                if (expandable) {
                    expandState = it
                }
            },
            onChangeEditState = {
                if (editable) {
                    editState = it
                }
            },
            onUpdateReminder = { savableReminder = it },
            onSaveReminder = {
                println("list - $savableReminder")
                onSaveReminder(savableReminder)
                expandState = false
                editState = false
            },
            onCancelReminder = {
                savableReminder = reminder
                expandState = false
                editState = false
            },
            onDeleteReminder = { onDeleteReminder() }
        )
    }


}
// Add Reminder Notification system

@Composable
private fun ReminderBoxView(
    reminder: Reminder,
    editState: Boolean,
    expandState: Boolean,
    journeyEndDate: Long,
    onChangeExpandState: (Boolean) -> Unit,
    onChangeEditState: (Boolean) -> Unit,
    onUpdateReminder: (Reminder) -> Unit,
    onSaveReminder: () -> Unit,
    onCancelReminder: () -> Unit,
    onDeleteReminder: () -> Unit
) {
    val selectedIndex by remember(reminder.dateType, reminder.reminderType) {
        mutableIntStateOf(
            getReminderDateTypeIndex(
                reminder.reminderType,
                reminder.dateType
            )
        )
    }

    if (editState) {

        ColumnSelectionBox(
            arrangement = Arrangement.SpaceEvenly,
            selectedIndex = selectedIndex,
            showIndent = true,
            borderStroke = BorderStroke(0.dp, Color.Transparent),
            color = MaterialTheme.colorScheme.secondaryContainer,
            buttons = {
                ReminderDateTypeSelectionButtons(
                    reminderType = reminder.reminderType,
                    onChangeReminderDateType = { onUpdateReminder(reminder.copy(dateType = it)) }
                )
            }
        ) {

            ReminderDateView(
                reminder = reminder,
                journeyEndDate = journeyEndDate,
                editState = true,
                expandState = expandState,
                onUpdateReminder = { onUpdateReminder(it) },
                onSave = {
                    println("reminder - $reminder")
                    onSaveReminder()
                },
                onCancel = { onCancelReminder() },
                onDelete = { onDeleteReminder() },
                onChangeEdit = { onChangeEditState(it) },
                onChangeExpand = { onChangeExpandState(it) }
            )


        }
    } else {
        ReminderDateView(
            reminder = reminder,
            journeyEndDate = journeyEndDate,
            editState = false,
            expandState = expandState,
            onUpdateReminder = { onUpdateReminder(it) },
            onSave = {
                println("reminder - $reminder")
                onSaveReminder()
            },
            onCancel = { onCancelReminder() },
            onDelete = { onDeleteReminder() },
            onChangeEdit = { onChangeEditState(it) },
            onChangeExpand = { onChangeExpandState(it) }
        )

    }

}

@Composable
private fun ReminderDateView(
    reminder: Reminder,
    journeyEndDate: Long,
    editState: Boolean,
    expandState: Boolean,
    onUpdateReminder: (Reminder) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    onChangeEdit: (Boolean) -> Unit,
    onChangeExpand: (Boolean) -> Unit
) {

    val outlineColor = MaterialTheme.colorScheme.outline

    val selectedReminderType by remember(reminder) {
        mutableIntStateOf(getReminderTypeIndex(reminder.reminderType))
    }

    val borderStrokeBrush by remember(editState) {
        mutableStateOf(
            if (editState) {
                Brush.horizontalGradient(
                    listOf(Color.Transparent, outlineColor)
                )
            } else {
                Brush.horizontalGradient(
                    listOf(Color.Transparent, Color.Transparent)
                )
            }
        )
    }

    ColumnSelectionBox(
        selectedIndex = selectedReminderType,
        alignment = ColumnBoxSelectionAlignment.END,
        showIndent = reminder.reminderType != ReminderType.All && editState,
        color = MaterialTheme.colorScheme.secondaryContainer,
        buttonSpacing = PaddingValues(horizontal = 6.dp),
        borderStroke = BorderStroke(2.dp, borderStrokeBrush),
        onClick = { onChangeExpand(!expandState) },
        buttons = {
            ReminderTypeView(
                editState = editState,
                reminderType = reminder.reminderType,
                onChangeReminder = { reminderType, reminderDateType ->
                    onUpdateReminder(
                        reminder.copy(
                            reminderType = reminderType,
                            dateType = reminderDateType
                        )
                    )
                }
            )
        }
    ) {

        AnimatedContent(targetState = expandState, label = "") { it ->
            if (it) {
                ReminderBoxExpanded(
                    reminder = reminder,
                    journeyEndDate = journeyEndDate,
                    editState = editState,
                    onUpdateReminder = { onUpdateReminder(it) },
                    onSave = { onSave() },
                    onCancel = { onCancel() },
                    onDelete = { onDelete() },
                    onChangeEdit = { onChangeEdit(it) }
                )
            } else {
                ReminderBoxUnexpanded(reminder = reminder)
            }
        }

    }
}

@Composable
private fun ReminderBoxUnexpanded(
    reminder: Reminder,

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (reminder.dateType) {
            ReminderDateType.EmptyDate -> {
                ReminderEmptyDateView(
                    reminder.endDate
                )
            }

            ReminderDateType.SelectedDate -> {
                ReminderSelectedDateView(
                    editState = false,
                    expandState = false,
                    startDate = reminder.startDate,
                    endDate = reminder.endDate,
                    reminderType = reminder.reminderType,
                    onChangeEndDate = { },
                    onChangeStartDate = { }
                )
            }

            ReminderDateType.SelectedDays -> {

                Spacer(modifier = Modifier.width(6.dp))

                ReminderSelectedDaysView(
                    editState = false,
                    expandState = false,
                    selectedDaysList = reminder.selectedDays,
                    onUpdateList = {  }
                )

            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = reminder.title,
            fontFamily = FontFamily(Font(R.font.rubik_font)),
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
private fun ReminderBoxExpanded(
    reminder: Reminder,
    journeyEndDate: Long,
    editState: Boolean,
    onUpdateReminder: (Reminder) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    onChangeEdit: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, top = 6.dp)
    ) {
        when (reminder.dateType) {
            ReminderDateType.EmptyDate -> {
                if(editState){
                    onUpdateReminder(reminder.copy(endDate = journeyEndDate))
                }
                ReminderEmptyDateView(endDate = reminder.endDate)
            }

            ReminderDateType.SelectedDate -> {
                ReminderSelectedDateView(
                    editState = editState,
                    expandState = true,
                    startDate = reminder.startDate,
                    endDate = reminder.endDate,
                    reminderType = reminder.reminderType,
                    onChangeEndDate = { onUpdateReminder(reminder.copy(endDate = it)) },
                    onChangeStartDate = { onUpdateReminder(reminder.copy(startDate = it)) }
                )
            }

            ReminderDateType.SelectedDays -> {
                ReminderSelectedDaysView(
                    editState = editState,
                    selectedDaysList = reminder.selectedDays,
                    expandState = true,
                    onUpdateList = { onUpdateReminder(reminder.copy(selectedDays = it)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        ReminderAssistText(
            reminderType = reminder.reminderType,
            reminderDateType = reminder.dateType,
            startDate = reminder.startDate,
            endDate = reminder.endDate
        )

        Spacer(modifier = Modifier.height(18.dp))


        ReminderTextBoxView(
            text = reminder.title,
            onValueChange = { onUpdateReminder(reminder.copy(title = it)) },
            editState = editState,
            textLabel = "Title",
            gapLength = 40.dp,
            fontSize = 18,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        ReminderTextBoxView(
            text = reminder.description,
            onValueChange = { onUpdateReminder(reminder.copy(description = it)) },
            editState = editState,
            textLabel = "Description",
            gapLength = 85.dp,
            fontWeight = FontWeight.Medium,
            fontSize = 16
        )

        Spacer(modifier = Modifier.height(12.dp))

        ReminderBoxButtons(
            modifier = Modifier.align(Alignment.End),
            editState = editState,
            onClickSave = { onSave() },
            onClickCancel = { onCancel() },
            onClickEdit = { onChangeEdit(!editState) },
            onClickDelete = { onDelete() }
        )

    }
}


@Composable
private fun ReminderEmptyDateView(
    endDate: Long
) {

    val text by remember(endDate){
        mutableStateOf(
            if(endDate < 0){
                "Select Journey Date"
            }else{
                DateHandler().getDaysLeft(endDate).toString()
            }
        )
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.rubik_font)),
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun ReminderSelectedDateView(
    editState: Boolean,
    expandState: Boolean,
    startDate: Long,
    endDate: Long,
    reminderType: ReminderType,
    onChangeStartDate: (Long) -> Unit,
    onChangeEndDate: (Long) -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val datePickerState = rememberDatePickerState()

    var showDatePicker by remember {
        mutableStateOf(ReminderDatePicker())
    }

    val startDateText by remember(startDate) {
        mutableStateOf(DateHandler(startDate).getText())
    }

    val endDateText by remember(endDate) {
        mutableStateOf(DateHandler(endDate).getText())
    }

    val fontSize by remember(expandState) {
        mutableStateOf(
            if (expandState) {
                18.sp
            } else {
                12.sp
            }
        )
    }

    val annotatedStartDateString by remember(startDateText) {
        mutableStateOf(buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                append(startDateText.subSequence(0, 2))
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append(startDateText.subSequence(2, 5))
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                append(startDateText.subSequence(5, 9))
            }
        })
    }

    val annotatedEndDateString by remember(endDateText) {
        mutableStateOf(buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                append(endDateText.subSequence(0, 2))
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append(endDateText.subSequence(2, 5))
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                append(endDateText.subSequence(5, 9))
            }
        })
    }

    AnimatedVisibility(visible = showDatePicker.show) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = ReminderDatePicker() },
            confirmButton = {

                TextButton(onClick = { showDatePicker = ReminderDatePicker() }) {
                    Text(text = "Cancel")
                }

                FilledTonalButton(onClick = {
                    when (showDatePicker.reminderDate) {
                        ReminderDate.START -> onChangeStartDate(
                            datePickerState.selectedDateMillis ?: 0L
                        )

                        ReminderDate.END -> onChangeEndDate(
                            datePickerState.selectedDateMillis ?: 0L
                        )
                    }
                    showDatePicker = ReminderDatePicker()
                }) {
                    Text(text = "Set")
                }

            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row {
        Box(
            modifier = Modifier
                .height(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable(interactionSource, indication = null) {
                    if (editState) {
                        showDatePicker = ReminderDatePicker(true)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = annotatedStartDateString,
                fontFamily = FontFamily(Font(R.font.rubik_font)),
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(
            modifier = Modifier.width(
                if (expandState) {
                    12.dp
                } else {
                    2.dp
                }
            )
        )

        if(reminderType != ReminderType.Birthday){
            AnimatedContent(endDate > startDate, label = "") { show ->
                if (show) {
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable(interactionSource, indication = null) {
                                showDatePicker =
                                    ReminderDatePicker(show = true, reminderDate = ReminderDate.END)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 6.dp),
                            text = annotatedEndDateString,
                            fontFamily = FontFamily(Font(R.font.rubik_font)),
                            fontSize = fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                } else {
                    AnimatedVisibility(visible = editState) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                                    shape = CircleShape
                                )
                                .clickable(interactionSource, indication = null) {
                                    showDatePicker =
                                        ReminderDatePicker(
                                            show = true,
                                            reminderDate = ReminderDate.END
                                        )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontFamily = FontFamily(Font(R.font.rubik_font)),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                }
            }
        }
    }

}

@Composable
private fun ReminderSelectedDaysView(
    editState: Boolean,
    expandState: Boolean,
    selectedDaysList: List<Int>,
    onUpdateList: (List<Int>) -> Unit
) {
    val dateHandler by remember {
        mutableStateOf(DateHandler())
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    val animatedSize = animateDpAsState(
        targetValue = if (expandState) {
            28.dp
        } else {
            6.dp
        }, label = ""
    )

    Box(
        modifier = Modifier
            .height(animatedSize.value + 4.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        LazyRow(
            modifier = Modifier.padding(start = 6.dp)
        ) {
            items(
                count = 7,
                itemContent = { itemIndex ->

                    val animatedBoxColor = animateColorAsState(
                        targetValue = if (selectedDaysList[itemIndex] == 1) {
                            MaterialTheme.colorScheme.tertiaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        },
                        label = ""
                    )

                    Box(
                        modifier = Modifier
                            .size(animatedSize.value)
                            .background(color = animatedBoxColor.value, shape = CircleShape)
                            .clickable(interactionSource, indication = null) {
                                if (editState) {
                                    onUpdateList(
                                        selectedDaysList
                                            .toMutableList()
                                            .mapIndexed { index, i ->
                                                if (index == itemIndex) {
                                                    if (i == 0) {
                                                        1
                                                    } else {
                                                        0
                                                    }
                                                } else {
                                                    i
                                                }
                                            }
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (expandState) {
                            Text(
                                text = dateHandler.getDayFromInt(itemIndex).subSequence(0, 3)
                                    .toString(),
                                fontFamily = FontFamily(Font(R.font.rubik_font)),
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(6.dp))


                }
            )
        }

    }

}

@Composable
private fun ReminderTextBoxView(
    text: String,
    onValueChange: (String) -> Unit,
    editState: Boolean,
    gapLength: Dp,
    textLabel: String,
    fontSize: Int,
    fontWeight: FontWeight
) {

    if (editState) {
        DaterTextField(
            text = text,
            onValueChange = { onValueChange(it) },
            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
            fontFamily = FontFamily(Font(R.font.rubik_font)),
            label = textLabel,
            gapLength = gapLength
        )
    } else {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.rubik_font)),
            fontWeight = fontWeight,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }

}

@Composable
private fun ReminderBoxButtons(
    modifier: Modifier = Modifier,
    editState: Boolean,
    onClickSave: () -> Unit,
    onClickCancel: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit
) {

    Row(modifier = modifier) {
        if (editState) {
            TextButton(onClick = { onClickCancel() }) {
                Text(text = "Cancel")
            }

            Button(onClick = { onClickSave() }) {
                Text(text = "Save")
            }
        } else {
            TextButton(onClick = { onClickDelete() }) {
                Text(text = "Delete")
            }

            Button(
                onClick = { onClickEdit() }) {
                Text(text = "Edit")
            }
        }


    }

}

@Composable
private fun ReminderDateTypeSelectionButtons(
    reminderType: ReminderType,
    onChangeReminderDateType: (ReminderDateType) -> Unit
) {


    if (reminderType == ReminderType.Event) {
        IconButton(

            onClick = { onChangeReminderDateType(ReminderDateType.EmptyDate) }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.reminder_datetype_all),
                contentDescription = ""
            )
        }
    }





    IconButton(
        onClick = { onChangeReminderDateType(ReminderDateType.SelectedDate) }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.reminder_datetype_dates),
            contentDescription = ""
        )
    }




    if (reminderType == ReminderType.Alert) {
        IconButton(
            onClick = { onChangeReminderDateType(ReminderDateType.SelectedDays) }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.reminder_datetype_days),
                contentDescription = ""
            )
        }
    }

}

@Composable
private fun ReminderTypeView(
    editState: Boolean,
    reminderType: ReminderType,
    onChangeReminder: (ReminderType, ReminderDateType) -> Unit
) {
    AnimatedVisibility(visible = editState || reminderType == ReminderType.Alert) {
        ReminderAlertAnimation(
            modifier = Modifier.size(28.dp),
            onClick = {
                onChangeReminder(ReminderType.Alert, ReminderDateType.SelectedDate)
            }
        )
    }

    AnimatedVisibility(visible = editState || reminderType == ReminderType.Birthday) {
        ReminderBirthdayAnimation(
            modifier = Modifier.size(28.dp),
            onClick = {
                onChangeReminder(ReminderType.Birthday, ReminderDateType.SelectedDate)
            }
        )
    }

    AnimatedVisibility(visible = editState || reminderType == ReminderType.Event) {
        ReminderEventAnimation(
            modifier = Modifier.size(28.dp),
            onClick = {
                onChangeReminder(ReminderType.Event, ReminderDateType.EmptyDate)
            }
        )
    }


}

// Add Date Picker || Title and Description and other column Selection Box
private fun getReminderDateTypeIndex(
    reminderType: ReminderType,
    reminderDateType: ReminderDateType
): Int {
    when (reminderType) {
        ReminderType.Alert -> {
            return when (reminderDateType) {
                ReminderDateType.EmptyDate -> 0
                ReminderDateType.SelectedDate -> 0
                ReminderDateType.SelectedDays -> 1
            }
        }

        ReminderType.All -> {
            return when (reminderDateType) {
                ReminderDateType.EmptyDate -> 0
                ReminderDateType.SelectedDate -> 1
                ReminderDateType.SelectedDays -> 2
            }
        }

        ReminderType.Birthday -> {
            return when (reminderDateType) {
                ReminderDateType.EmptyDate -> 0
                ReminderDateType.SelectedDate -> 0
                ReminderDateType.SelectedDays -> 0
            }
        }

        ReminderType.Event -> {
            return when (reminderDateType) {
                ReminderDateType.EmptyDate -> 0
                ReminderDateType.SelectedDate -> 1
                ReminderDateType.SelectedDays -> 2
            }
        }
    }
}

@Composable
private fun ReminderAssistText(
    reminderType: ReminderType,
    reminderDateType: ReminderDateType,
    startDate: Long,
    endDate: Long
) {

    val startDateText by remember(startDate) {
        mutableStateOf(DateHandler(startDate).getText())
    }

    val endDateText by remember(endDate) {
        mutableStateOf(DateHandler(endDate).getText())
    }

    val text by remember(reminderDateType, reminderType,endDateText,startDateText) {
        mutableStateOf(
            when (reminderType) {
                ReminderType.Alert -> {
                    when (reminderDateType) {
                        ReminderDateType.EmptyDate -> ""
                        ReminderDateType.SelectedDate -> {
                            if (endDate > startDate) {
                                "Notified on $startDateText and $endDateText"
                            } else {
                                "Notified on $startDateText"
                            }
                        }

                        ReminderDateType.SelectedDays -> "Notified on the selected days"
                    }
                }

                ReminderType.All -> ""
                ReminderType.Birthday -> {
                    "Notified from 3 days prior to $startDateText"
                }

                ReminderType.Event -> {
                    when (reminderDateType) {
                        ReminderDateType.EmptyDate -> "Notified Each Day"
                        ReminderDateType.SelectedDate -> {
                            "Notified each day from $startDateText to $endDateText"
                        }

                        ReminderDateType.SelectedDays -> ""
                    }
                }
            }
        )
    }

    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.rubik_font)),
        fontSize = 12.sp,
        fontWeight = FontWeight.Thin,
        fontStyle = FontStyle.Italic
    )

}

private fun getReminderTypeIndex(reminderType: ReminderType): Int {
    return when (reminderType) {
        ReminderType.Alert -> 0
        ReminderType.All -> 0
        ReminderType.Birthday -> 1
        ReminderType.Event -> 2
    }
}


private data class ReminderDatePicker(
    val show: Boolean = false,
    val reminderDate: ReminderDate = ReminderDate.START
)

private enum class ReminderDate {
    START,
    END
}

