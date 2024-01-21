package com.example.dater.Data.Reminder.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.dater.R


sealed class ReminderType {
    object Event : ReminderType()
    object Birthday : ReminderType()
    object Alert : ReminderType()
    object All : ReminderType()
}

fun getReminderIndex(reminderType: ReminderType): Int {
    return when (reminderType) {
        ReminderType.Alert -> 0
        ReminderType.All -> -1
        ReminderType.Birthday -> 1
        ReminderType.Event -> 2
    }
}

fun getReminderFromIndex(index: Int): ReminderType {
    return when (index) {
        0 -> ReminderType.Alert
        1 -> ReminderType.Birthday
        2 -> ReminderType.Event
        else -> ReminderType.All
    }
}

@Composable
fun ReminderTypeIconView(
    showAll: Boolean,
    selectedReminderType: Int,
    selectedReminderColor: Color = Color.Unspecified,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    onSelectReminderType: (ReminderType) -> Unit
) {
    AnimatedVisibility(visible = selectedReminderType == 0 || showAll) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.reminder_alert_icon),
            contentDescription = "",
            tint = if (selectedReminderType == 0 && selectedReminderColor.isSpecified) {
                selectedReminderColor
            } else {
                iconColor
            },
            modifier = Modifier
                .padding(end = 6.dp, top = 6.dp, bottom = 6.dp)
                .clickable {
                onSelectReminderType(ReminderType.Alert)
            }
        )
    }

    AnimatedVisibility(visible = selectedReminderType == 1 || showAll) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.reminder_birthday_icon),
            contentDescription = "",
            tint = if (selectedReminderType == 1 && selectedReminderColor.isSpecified) {
                selectedReminderColor
            } else {
                iconColor
            },
            modifier = Modifier
                .padding(end = 6.dp, top = 6.dp, bottom = 6.dp)
                .clickable {
                onSelectReminderType(ReminderType.Birthday)
            }
        )
    }

    AnimatedVisibility(visible = selectedReminderType == 2 || showAll) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.reminder_events_icon),
            contentDescription = "",
            tint = if (selectedReminderType == 2 && selectedReminderColor.isSpecified) {
                selectedReminderColor
            } else {
                iconColor
            },
            modifier = Modifier
                .padding(end = 6.dp, top = 6.dp, bottom = 6.dp)
                .clickable {
                onSelectReminderType(ReminderType.Event)
            }
        )
    }
}