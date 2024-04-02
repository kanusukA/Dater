@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dater.ui.components.MyDatePicker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleDatePicker(
    datePickerState: DatePickerState,
    show: Boolean,
    dismiss: () -> Unit,
    setDate: (Long) -> Unit
){
    AnimatedVisibility(visible = show){
        DatePickerDialog(onDismissRequest = { dismiss() }, confirmButton = {
            Row() {

                TextButton(onClick = { dismiss() }) {
                    Text(text = "Cancel")
                }

                Spacer(modifier = Modifier.width(12.dp))

                FilledTonalButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        setDate(it)
                    }
                }) {
                    Text(text = "Set")
                }
            }
        }) {
            DatePicker(state = datePickerState)

        }
    }

}