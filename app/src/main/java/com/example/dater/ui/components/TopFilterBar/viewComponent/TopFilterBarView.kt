@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dater.ui.components.TopFilterBar.viewComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dater.R
import com.example.dater.ui.components.TopFilterBar.TopFilterBarEvents
import com.example.dater.ui.components.TopFilterBar.TopFilterBarState
import com.example.dater.ui.components.TopFilterBar.TopFilterBarViewModel
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType


@Composable
fun TopFilterBarView(
    modifier: Modifier = Modifier,
    viewModel: TopFilterBarViewModel,
) {

    val search = viewModel.search.collectAsState().value
    val state = viewModel.state.collectAsState().value
    val stateString = viewModel.stateString.collectAsState().value
    val toggleFilter = viewModel.toggleFilter.collectAsState().value
    val toggleSearch = viewModel.toggleSearch.collectAsState().value

    Box(
        modifier = modifier
            .height(
                if (toggleFilter) {
                    135.dp
                } else {
                    100.dp
                }
            )
    ) {

        FilterBarSearch(
            state = state,
            onClickSelectedState = {
                viewModel.onEvent(TopFilterBarEvents.ChangeFilterType(it))
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(20.dp)
                )
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(4f)
                    .clickable { viewModel.onEvent(TopFilterBarEvents.ToggleSearch) },
                contentAlignment = Alignment.Center
            ) {
                if (toggleSearch) {
                    OutlinedTextField(
                        value = search.text,
                        onValueChange = { viewModel.onEvent(TopFilterBarEvents.ChangeSearchText(it)) },
                        label = { Text(text = search.labelText) }
                    )
                } else {
                    Text(
                        text = stateString,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                    ),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                IconButton(onClick = { viewModel.onEvent(TopFilterBarEvents.ChangeState) }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.home_scrollup_icon), contentDescription = "Scroll up")
                }
                IconButton(onClick = { viewModel.onEvent(TopFilterBarEvents.ToggleFilter) }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.home_filter_icon), contentDescription = "Filter")
                }
            }
        }
    }

}


@Composable
private fun FilterBarSearch(
    state: TopFilterBarState,
    onClickSelectedState: (TopFilterBarState) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier

            .background(
                Color.DarkGray,
                shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            when (state) {
                is TopFilterBarState.JourneyState -> {
                    IconButton(onClick = {
                        onClickSelectedState(
                            TopFilterBarState.JourneyState(
                                TopFilterBarJourneyType.Descending
                            )
                        )
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.home_scrollup_icon),
                            contentDescription = ""
                        )
                    }
                    IconButton(onClick = {
                        onClickSelectedState(
                            TopFilterBarState.JourneyState(
                                TopFilterBarJourneyType.Ascending
                            )
                        )
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.home_filter_icon),
                            contentDescription = ""
                        )
                    }

                }

                is TopFilterBarState.ReminderState -> {
                    IconButton(onClick = {
                        onClickSelectedState(
                            TopFilterBarState.ReminderState(
                                TopFilterBarReminderType.Event
                            )
                        )
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.reminder_events_icon),
                            contentDescription = "Event"
                        )
                    }
                    IconButton(onClick = {
                        onClickSelectedState(
                            TopFilterBarState.ReminderState(
                                TopFilterBarReminderType.Birthday
                            )
                        )
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.reminder_birthday_icon),
                            contentDescription = "BirthDay"
                        )
                    }
                    IconButton(onClick = {
                        onClickSelectedState(
                            TopFilterBarState.ReminderState(
                                TopFilterBarReminderType.Alert
                            )
                        )
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.reminder_alert_icon),
                            contentDescription = "Alert"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TopFilterBarPreview() {
//    TopFilterBarView()
}

@Preview
@Composable
fun PreviewFilterBar() {
    FilterBarSearch(TopFilterBarState.JourneyState(TopFilterBarJourneyType.Descending))
}
