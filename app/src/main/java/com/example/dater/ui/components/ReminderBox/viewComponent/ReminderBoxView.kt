@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dater.ui.components.ReminderBox.viewComponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.Data.Reminder.domain.model.ReminderDateType
import com.example.dater.Data.Reminder.utils.ReminderType
import com.example.dater.Data.Reminder.utils.ReminderTypeIconView
import com.example.dater.Data.Reminder.utils.getReminderIndex
import com.example.dater.Data.utils.DateHandler
import com.example.dater.Data.utils.TextLength
import com.example.dater.Data.utils.getNextReminderWeekDay
import com.example.dater.R
import com.example.dater.notification.reminderNotifier.ReminderNotifierRequest
import com.example.dater.ui.components.ReminderBox.ReminderErrorCheck
import com.example.dater.ui.components.SelectionBox.ColumnSelectionBox
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionAlignment

//Add Reminder Completion and Journey Completion
@Composable
fun ReminderBoxView(
    reminder: Reminder,
    editable: Boolean,
    expandable: Boolean,
    initialEditState: Boolean,
    initialExpandState: Boolean,
    onSaveReminder: (Reminder) -> Unit,
    onDeleteReminder: () -> Unit,
    journeyEndDate: Long = 0L,
) {

    val context = LocalContext.current

    var savedReminder by remember {
        mutableStateOf(reminder)
    }
    var editState by remember {
        mutableStateOf(initialEditState)
    }
    var expandState by remember {
        mutableStateOf(initialExpandState)
    }

    val errorCheck by remember(key1 = savedReminder) {
        mutableStateOf(
            ReminderErrorCheck(
                context,
                savedReminder,
                onUpdateReminder = { savedReminder = it },
                onSaveReminder = {
                    onSaveReminder(it)
                    editState = false
                    expandState = false
                }
            )

        )
    }

    fun onCancel(){
        println("reminder title - ${reminder.title}")
        if (reminder.title.isBlank()){
            onDeleteReminder()
        } else {
            savedReminder = reminder
            editState = false
            expandState = false
        }
    }


    LaunchedEffect(key1 = journeyEndDate, key2 = savedReminder) {
        if (savedReminder.dateType == ReminderDateType.EmptyDate && journeyEndDate > DateHandler().getLong()) {
            errorCheck.endDate(journeyEndDate)
        }
    }

    AnimatedContent(targetState = editState, label = "") { edit ->
        if (edit) {
            ColumnSelectionBox(
                selectedIndex = getIndexFromDateType(savedReminder.dateType),
                showIndent = editState,
                arrangement = Arrangement.Center,
                buttons = {

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reminder_datetype_all),
                        contentDescription = "Every Day",
                        modifier = Modifier
                            .padding(start = 6.dp, end = 3.dp, top = 8.dp, bottom = 8.dp)
                            .clickable {
                                savedReminder =
                                    savedReminder.copy(dateType = ReminderDateType.EmptyDate)
                            }
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reminder_datetype_dates),
                        contentDescription = "Selected Date",
                        modifier = Modifier
                            .padding(start = 6.dp, end = 3.dp, top = 8.dp, bottom = 8.dp)
                            .clickable {
                                savedReminder =
                                    savedReminder.copy(dateType = ReminderDateType.SelectedDate)
                            }
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reminder_datetype_days),
                        contentDescription = "Selected Days",
                        modifier = Modifier
                            .padding(start = 6.dp, end = 3.dp, top = 8.dp, bottom = 8.dp)
                            .clickable {
                                savedReminder =
                                    savedReminder.copy(dateType = ReminderDateType.SelectedDays)
                            }
                    )

                }

            ) {
                reminderBoxView(
                    savedReminder = savedReminder,
                    editMode = editState,
                    expand = expandState,
                    selectedReminder = savedReminder.reminderType,
                    onChangeSelectedReminder = {
                        savedReminder = savedReminder.copy(reminderType = it)
                    },
                    onChangeExpanded = {
                        if (expandable) {
                            expandState = it
                        }
                    },
                    onChangeEditMode = {
                        if (editable) {
                            editState = it
                        }
                    },
                    onSaveReminder = { errorCheck.saveReminder() },
                    onCancelReminder = { onCancel() },
                    onDeleteReminder = { onDeleteReminder() },
                    onChangeTitle = { errorCheck.title(it) },
                    onChangeDescription = { errorCheck.description(it) },
                    onChangeStartDate = { errorCheck.startDate(it) },
                    onChangeEndDate = { errorCheck.endDate(it) },
                    onChangeSelectedDays = { savedReminder = savedReminder.copy(selectedDays = it) }
                )

            }
        } else {

            reminderBoxView(
                savedReminder = savedReminder,
                editMode = editState,
                expand = expandState,
                selectedReminder = savedReminder.reminderType,
                onChangeSelectedReminder = {
                    savedReminder = savedReminder.copy(reminderType = it)
                },
                onChangeExpanded = {
                    if (expandable) {
                        expandState = it
                    }
                },
                onChangeEditMode = {
                    if (editable) {
                        editState = it
                    }
                },
                onSaveReminder = { errorCheck.saveReminder() },
                onCancelReminder = { onCancel() },
                onDeleteReminder = { onDeleteReminder() },
                onChangeTitle = { errorCheck.title(it) },
                onChangeDescription = { errorCheck.description(it) },
                onChangeStartDate = { errorCheck.startDate(it) },
                onChangeEndDate = { errorCheck.endDate(it) },
                onChangeSelectedDays = { savedReminder = savedReminder.copy(selectedDays = it) }
            )

        }

    }



}

@Composable
private fun reminderBoxView(
    savedReminder: Reminder,
    editMode: Boolean,
    expand: Boolean,
    selectedReminder: ReminderType,
    onChangeSelectedReminder: (ReminderType) -> Unit,
    onChangeExpanded: (Boolean) -> Unit,
    onChangeEditMode: (Boolean) -> Unit,
    onChangeTitle: (String) -> Unit,
    onChangeDescription: (String) -> Unit,
    onChangeStartDate: (Long) -> Unit,
    onChangeEndDate: (Long) -> Unit,
    onChangeSelectedDays: (List<Int>) -> Unit,
    onSaveReminder: () -> Unit,
    onCancelReminder: () -> Unit,
    onDeleteReminder: () -> Unit
) {
    val context = LocalContext.current
    val notifierRequest = ReminderNotifierRequest(context, savedReminder.id)

    var completed by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true){
        if (savedReminder.dateType != ReminderDateType.SelectedDays){
            completed = if (savedReminder.endDate == 0L) {
                DateHandler().getLong() >= savedReminder.startDate
            } else {
                DateHandler().getLong() >= savedReminder.endDate
            }
        }
    }

    ColumnSelectionBox(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        alignment = ColumnBoxSelectionAlignment.END,
        arrangement = Arrangement.Center,
        selectedIndex = getReminderIndex(selectedReminder),
        showIndent = editMode,
        onClick = {
            if (!editMode) {
                onChangeExpanded(!expand)
            }
        },
        buttons = {
            ReminderTypeIconView(
                showAll = editMode,
                iconColor = MaterialTheme.colorScheme.onSurface,
                selectedReminderColor = MaterialTheme.colorScheme.onSurface,
                selectedReminderType = getReminderIndex(savedReminder.reminderType),
                onSelectReminderType = { onChangeSelectedReminder(it) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                AnimatedContent(targetState = !completed || expand,
                    label = ""
                ){ complete ->
                    if (complete){
                        Row(verticalAlignment = Alignment.CenterVertically){
                            ReminderBoxDate(
                                reminder = savedReminder,
                                expand = expand,
                                editMode = editMode,
                                onChangeStartDate = {
                                    onChangeStartDate(it.getLong())
                                },
                                onChangeEndDate = {
                                    onChangeEndDate(it.getLong())
                                },
                                onChangeSelectedDays = {
                                    onChangeSelectedDays(it)
                                }
                            )
                        }
                    } else {
                        ReminderBoxCompleted()
                    }
                }


                Spacer(modifier = Modifier.width(24.dp))

                AnimatedVisibility(
                    visible = !expand,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = savedReminder.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // REMINDER TITLE
            AnimatedVisibility(visible = expand) {

                if (editMode) {
                    OutlinedTextField(
                        value = savedReminder.title,
                        onValueChange = { onChangeTitle(it) },
                        label = { Text(text = "Title") },
                        modifier = Modifier.padding(12.dp)
                    )
                } else {
                    Text(
                        text = savedReminder.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp)
                    )
                }
            }
            AnimatedVisibility(visible = expand) {

                if (editMode) {
                    OutlinedTextField(
                        value = savedReminder.description,
                        onValueChange = {
                            onChangeDescription(it)
                        },
                        label = { Text(text = "Description") },
                        modifier = Modifier.padding(start = 12.dp)
                    )
                } else {
                    Text(
                        text = savedReminder.description,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = expand,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = {
                        notifierRequest.getReminderNotifierStatusToast()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.debug_info_icon),
                            contentDescription = ""
                        )
                    }

                    AnimatedContent(targetState = editMode, label = "") {
                        if (it) {
                            TextButton(onClick = {
                                onCancelReminder()
                            }
                            ) {
                                Text(text = "Cancel")
                            }
                        } else {
                            TextButton(onClick = {
                                onChangeEditMode(true)
                                onChangeExpanded(true)
                            }
                            ) {
                                Text(text = "Edit")
                            }
                        }
                    }
                    AnimatedContent(targetState = editMode, label = "") {
                        if (it) {
                            Button(onClick = {
                                onSaveReminder()
                            }
                            ) {
                                Text(text = "Save")
                            }
                        } else {
                            Button(onClick = { onDeleteReminder() }) {
                                Text(text = "Delete")
                            }
                        }
                    }

                }

            }

        }

    }

}


@Composable
private fun ReminderBoxDate(
    reminder: Reminder,
    expand: Boolean,
    editMode: Boolean,
    onChangeStartDate: (DateHandler) -> Unit,
    onChangeEndDate: (DateHandler) -> Unit,
    onChangeSelectedDays: (List<Int>) -> Unit
) {

    var dateLength by remember {
        mutableStateOf(getDateLength(reminder.startDate, reminder.endDate))
    }

    val spacer = animateDpAsState(
        targetValue = if (expand) {
            12.dp
        } else {
            6.dp
        }, label = ""
    )

    var addEndDate by remember {
        mutableStateOf(reminder.endDate == 0L)
    }

    val textPaddingValues by remember {
        mutableStateOf(
            if (expand) {
                PaddingValues(all = 6.dp)
            } else {
                PaddingValues(vertical = 2.dp, horizontal = 6.dp)
            }
        )
    }

    val boxPaddingValues by remember {
        mutableStateOf(
            if (expand) {
                PaddingValues(4.dp)
            } else {
                PaddingValues()
            }
        )
    }

    val fontSize by remember {
        mutableStateOf(
            if (expand) {
                16
            } else {
                12
            }
        )
    }

    var startDate by remember {
        mutableStateOf(
            if (reminder.startDate != 0L) {
                DateHandler(reminder.startDate).getText()
            } else {
                "DD/MM/YYYY"
            }
        )
    }

    var endDate by remember {
        mutableStateOf(
            if (reminder.endDate != 0L) {
                DateHandler(reminder.endDate).getText(dateLength = dateLength)
            } else {
                "DD/MM/YYYY"
            }
        )
    }

    LaunchedEffect(key1 = expand){
        if (expand){
            startDate = DateHandler(reminder.startDate).getText()
            endDate = DateHandler(reminder.endDate).getText()
        } else {
            startDate = DateHandler(reminder.startDate).getText(dateLength = dateLength)
            endDate = DateHandler(reminder.endDate).getText(dateLength = dateLength)
        }
    }

    LaunchedEffect(key1 = reminder.startDate, key2 = reminder.endDate) {

        dateLength = getDateLength(reminder.startDate, reminder.endDate)

        startDate = if (reminder.startDate != 0L) {
            DateHandler(reminder.startDate).getText(dateLength = dateLength)
        } else {
            "DD/MM/YYYY"
        }

        endDate = if (reminder.endDate != 0L) {
            DateHandler(reminder.endDate).getText(dateLength = dateLength)
        } else {
            "DD/MM/YYYY"
        }

    }


    when (reminder.dateType) {

        ReminderDateType.EmptyDate -> {

            ReminderBoxEveryDate(
                reminder = reminder,
                editMode = editMode,
                textPaddingValues = textPaddingValues,
                boxPaddingValues = boxPaddingValues,
                fontSize = fontSize
            )
        }

        ReminderDateType.SelectedDate -> {

            ReminderBoxSelectedDate(
                date = startDate,
                editMode = editMode,
                fontSize = fontSize,
                textPaddingValues = textPaddingValues,
                boxPaddingValues = boxPaddingValues,
                onChangeDate = {
                    onChangeStartDate(it)
                }
            )

            Spacer(modifier = Modifier.width(spacer.value))

            AnimatedVisibility(visible = reminder.startDate > 0L && (!addEndDate || editMode)) {

                AnimatedContent(targetState = addEndDate, label = "") {

                    if (it) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                    shape = CircleShape
                                )
                                .border(
                                    BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                                    shape = CircleShape
                                )
                                .clickable { addEndDate = false },
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "+",
                                fontWeight = FontWeight.Bold
                            )

                        }
                    } else {
                        ReminderBoxSelectedDate(
                            date = endDate,
                            editMode = editMode,
                            textPaddingValues = textPaddingValues,
                            boxPaddingValues = boxPaddingValues,
                            fontSize = fontSize,
                            onChangeDate = { date ->
                                onChangeEndDate(date)
                            }
                        )
                    }
                }
            }

        }

        ReminderDateType.SelectedDays -> {
            ReminderBoxSelectedDays(
                reminder = reminder,
                editMode = editMode,
                onChangeSelectedDays = { onChangeSelectedDays(it) }
            )
        }
    }
}

@Composable
private fun ReminderBoxCompleted(){
    Box(modifier = Modifier
        .background(color = Color.Green, shape = CircleShape)
        .border(BorderStroke(3.dp, color = MaterialTheme.colorScheme.outline), shape = CircleShape)
    ){
        Text(
            text = "Passed",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp)
        )

    }
}


@Composable
private fun ReminderBoxSelectedDate(
    date: String,
    editMode: Boolean,
    fontSize: Int,
    boxPaddingValues: PaddingValues,
    textPaddingValues: PaddingValues,
    onChangeDate: (DateHandler) -> Unit
) {

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            TextButton(
                onClick = {
                    onChangeDate(DateHandler(datePickerState.selectedDateMillis ?: 0L))
                    showDatePicker = false
                }
            ) {
                Text(text = "Okay")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    Box(modifier = Modifier
        .padding(boxPaddingValues)
        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.outline), shape = CircleShape)
        .clickable {
            if (editMode) {
                showDatePicker = true
            }
        }
    ) {
        Text(
            text = date,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(textPaddingValues)
        )
    }

}

@Composable
private fun ReminderBoxEveryDate(
    reminder: Reminder,
    editMode: Boolean,
    fontSize: Int,
    textPaddingValues: PaddingValues,
    boxPaddingValues: PaddingValues
) {

    var showMenu by remember {
        mutableStateOf(false)
    }

    var dateText by remember {
        mutableStateOf(
            if (reminder.endDate > DateHandler().getLong()) {
                ((reminder.endDate - DateHandler().getLong()) / 86400000L).toString()
            } else {
                "0"
            }
        )
    }

    LaunchedEffect(key1 = reminder.endDate) {
        dateText = if (reminder.endDate > DateHandler().getLong()) {
            ((reminder.endDate - DateHandler().getLong()) / 86400000L).toString()
        } else {
            "0"
        }
    }

    Box(modifier = Modifier
        .padding(boxPaddingValues)
        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.outline), shape = CircleShape)
        .clickable {
            if (editMode) {
                showMenu = true
            }
        }
    ) {

        Text(
            text = dateText,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(textPaddingValues)
                .align(Alignment.Center)

        )
    }

}

@Composable
private fun ReminderBoxSelectedDays(
    reminder: Reminder,
    editMode: Boolean,
    onChangeSelectedDays: (List<Int>) -> Unit
) {

    var selectedDays by remember {
        mutableStateOf(reminder.selectedDays)
    }
    val nextDay by remember {
        mutableStateOf(getNextReminderWeekDay(selectedDays))
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.primaryContainer

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(7) {

            val text by remember {
                mutableStateOf(
                    DateHandler().getDayFromInt(it).substring(IntRange(0, 2)).uppercase()
                )
            }

            var color by remember {
                mutableStateOf(secondaryColor)
            }

            val animatedColor = animateColorAsState(color, label = "")

            LaunchedEffect(key1 = selectedDays[it]) {

                color = if (selectedDays[it] == 1) {
                    primaryColor
                } else {
                    secondaryColor
                }
            }

            AnimatedVisibility(visible = editMode || nextDay == it) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .padding(3.dp)
                        .background(color = animatedColor.value, shape = CircleShape)
                        .border(
                            BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                            shape = CircleShape
                        )
                        .clickable {
                            if (editMode) {

                                selectedDays = if (selectedDays[it] == 0) {
                                    reminder.selectedDays.mapIndexed { index, value ->
                                        if (index == it) {
                                            value + 1
                                        } else {
                                            value
                                        }
                                    }
                                } else {
                                    reminder.selectedDays.mapIndexed { index, value ->
                                        if (index == it) {
                                            value - 1
                                        } else {
                                            value
                                        }
                                    }
                                }

                                onChangeSelectedDays(selectedDays)
                            }
                        }
                ) {
                    Text(
                        text = text,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        AnimatedVisibility(visible = !editMode) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                VerticalDivider(modifier = Modifier.height(28.dp))

                Spacer(modifier = Modifier.width(14.dp))

                repeat(7) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .padding(2.dp)
                            .background(
                                color = if (selectedDays[it] == 1) {
                                    primaryColor
                                } else {
                                    secondaryColor
                                }, shape = CircleShape
                            )
                    )
                }
            }
        }
    }

}


private fun getIndexFromDateType(dateType: ReminderDateType): Int {
    return when (dateType) {
        ReminderDateType.EmptyDate -> 0
        ReminderDateType.SelectedDate -> 1
        ReminderDateType.SelectedDays -> 2
    }
}


private fun getDateLength(startDate: Long, endDate: Long): TextLength {

    val startDateHandler = DateHandler(startDate)
    val endDateHandler = DateHandler(endDate)

    return if (startDateHandler.getMonth() == endDateHandler.getMonth() && DateHandler().getMonth() == startDateHandler.getMonth()) {
        if (startDateHandler.getYear() == endDateHandler.getYear() && DateHandler().getYear() == startDateHandler.getYear()) {
            TextLength.DAY
        } else {
            TextLength.YEAR
        }
    } else {
        if (startDateHandler.getYear() == endDateHandler.getYear() && DateHandler().getYear() == startDateHandler.getYear()) {
            TextLength.MONTH
        } else {
            TextLength.YEAR
        }
    }
}

