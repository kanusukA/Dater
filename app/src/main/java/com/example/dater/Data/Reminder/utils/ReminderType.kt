package com.example.dater.Data.Reminder.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderAlertAnimation
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderBirthdayAnimation
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderEventAnimation


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



@Composable
fun ReminderTypeIconView(
    showAll: Boolean,
    selectedReminderType: Int,
    showBackGround: Boolean = false,
    selectedReminderColor: Color = Color.Unspecified,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    onSelectReminderType: (ReminderType) -> Unit
) {



    AnimatedVisibility(
        visible = selectedReminderType == 0 || showAll
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            ReminderAlertAnimation(
                modifier = Modifier.size(26.dp),
                onClick = { onSelectReminderType(ReminderType.Alert) }
            )
        }

    }

    AnimatedVisibility(
        visible = selectedReminderType == 1 || showAll
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            ReminderBirthdayAnimation(
                modifier = Modifier.size(26.dp),
                onClick = { onSelectReminderType(ReminderType.Birthday) }
            )
        }
    }

    AnimatedVisibility(
        visible = selectedReminderType == 2 || showAll
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            ReminderEventAnimation(
                modifier = Modifier.size(26.dp),
                onClick = { onSelectReminderType(ReminderType.Event) }
            )
        }
    }
}