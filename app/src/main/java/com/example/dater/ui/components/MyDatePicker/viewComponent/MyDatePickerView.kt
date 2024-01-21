@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.dater.ui.components.MyDatePicker.viewComponent

import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dater.ui.components.MyDatePicker.MyDatePickerEvents
import com.example.dater.ui.components.MyDatePicker.MyDatePickerViewModel

@Composable
fun MyDatePickerView() {
    val viewModel = hiltViewModel<MyDatePickerViewModel>()
    val datePickerState = viewModel.datePickerState

    DatePickerDialog(onDismissRequest = { viewModel.onEvent(MyDatePickerEvents.Dismiss) },
        confirmButton = {
            Text(
                text = "OK",
                modifier = Modifier.clickable {
                    datePickerState.selectedDateMillis?.let { viewModel.onEvent(MyDatePickerEvents.Confirm(it)) }
                    }
                )
        }
    ) {
        DatePicker(state = datePickerState)

    }
}

@Preview
@Composable
fun PreviewDatePicker() {
    MyDatePickerView()
}