@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.example.dater.ui.components.TopFilterBar.viewComponent


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderAlertAnimation
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderBirthdayAnimation
import com.example.dater.ui.components.ReminderBox.animations.icon.ReminderEventAnimation
import com.example.dater.ui.components.SelectionBox.RowSelectionBox
import com.example.dater.ui.components.SelectionBox.Shapes.RowBoxSelectionShapeAlignment
import com.example.dater.ui.components.TopFilterBar.TopFilterBarEvents
import com.example.dater.ui.components.TopFilterBar.TopFilterBarSearchState
import com.example.dater.ui.components.TopFilterBar.TopFilterBarType
import com.example.dater.ui.components.TopFilterBar.TopFilterBarViewModel
import com.example.dater.ui.components.TopFilterBar.TopFilterButtonEvents
import com.example.dater.ui.components.TopFilterBar.animations.icon.TopFilterBarArrowToLensAnimation
import com.example.dater.ui.components.TopFilterBar.animations.icon.TopFilterBarFilterAscending
import com.example.dater.ui.components.TopFilterBar.animations.icon.TopFilterBarFilterDescending
import com.example.dater.ui.components.TopFilterBar.animations.icon.TopFilterBarFilterToCross
import com.example.dater.ui.components.TopFilterBar.shapes.TopBarSearchTextFieldInputs
import com.example.dater.ui.components.TopFilterBar.shapes.TopBarSearchTextFieldShape
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarJourneyType
import com.example.dater.ui.components.TopFilterBar.util.TopFilterBarReminderType


@Composable
fun TopFilterBarView(
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel<TopFilterBarViewModel>()
    val state = viewModel.state.collectAsState().value
    val stateString = viewModel.stateString.collectAsState().value
    val toggleFilter = viewModel.toggleFilter.collectAsState().value
    val selectedFilter = viewModel.selected.collectAsStateWithLifecycle().value
    val searchState = viewModel.searchState.collectAsState().value

    var filterBarHeight by remember {
        mutableStateOf(100.dp)
    }

    val filterBarAnimation = animateDpAsState(targetValue = filterBarHeight, label = "")

    LaunchedEffect(key1 = toggleFilter) {
        if (toggleFilter) {
            filterBarHeight = 145.dp
        } else {
            filterBarHeight = 100.dp
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .requiredHeight(filterBarAnimation.value)

    ) {

        // Implement selected filter and Filter changing
        topFilterBar(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            state = state,
            selectedFilter = selectedFilter,
            onChangeFilterType = { viewModel.onEvent(TopFilterBarEvents.ChangeFilterType(it)) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(100.dp)
                .align(Alignment.TopCenter)
                .shadow(4.dp, shape = RoundedCornerShape(30.dp))
        ) {

            topSearchBar(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                stateString = stateString,
                searchState = searchState,
                onClickShowSearch = { viewModel.onButtonEvents(TopFilterButtonEvents.EnableSearch) },
                onClickCloseSearch = { viewModel.onButtonEvents(TopFilterButtonEvents.DisableSearch) },
                onChangeSearchString = { viewModel.onEvent(TopFilterBarEvents.ChangeSearchText(it)) }
            )


            topSearchBarButtons(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp),
                searchState = searchState,
                onClickUp = { viewModel.onButtonEvents(TopFilterButtonEvents.ToggleState) },
                onClickFilter = { viewModel.onButtonEvents(TopFilterButtonEvents.ToggleFilter) },
                onClickSearch = { viewModel.onButtonEvents(TopFilterButtonEvents.Search) },
                onClickCancel = { viewModel.onButtonEvents(TopFilterButtonEvents.DisableSearch) }
            )


        }

    }

}

@Composable
private fun topSearchBar(
    modifier: Modifier = Modifier,
    stateString: String,
    searchState: TopFilterBarSearchState,
    onClickShowSearch: () -> Unit,
    onChangeSearchString: (String) -> Unit,
    onClickCloseSearch: () -> Unit
) {

    // Add search functionality

    var transitionX by remember {
        mutableFloatStateOf(0f)
    }

    var transitionY by remember {
        mutableFloatStateOf(0f)
    }

    var scale by remember {
        mutableFloatStateOf(0f)
    }

    val fontSize by remember(key1 = searchState.showState) {
        mutableIntStateOf(
            if (searchState.showState) {
                38
            } else {
                48
            }
        )
    }


    val animateTransitionX = animateFloatAsState(targetValue = transitionX, label = "")
    val animateTransitionY = animateFloatAsState(targetValue = transitionY, label = "")
    val animateScale = animateFloatAsState(targetValue = scale, label = "")
    val backgroundColor = animateColorAsState(
        targetValue = if(searchState.showState){
            Color.White.copy(alpha = 0.3f) }
        else{
            MaterialTheme.colorScheme.secondaryContainer
        }
        , label = ""
    )


    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp),
        onClick = { onClickShowSearch() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(
                    color = backgroundColor.value,
                    shape = TopBarSearchTextFieldShape(
                        TopBarSearchTextFieldInputs(
                            gapLength = 120.dp,
                            gapHeight = 2.dp,
                            paddingStart = 35.dp,
                            cornerRadius = 20.dp
                        )
                    )
                )
                .graphicsLayer {

                    this.translationX = animateTransitionX.value
                    this.translationY = animateTransitionY.value
                    this.scaleX = animateScale.value
                    this.scaleY = animateScale.value

                    if (searchState.showState) {

                        transitionX = -((this.size.width / 2f) - 122.dp.toPx())
                        transitionY = -this.size.height / 2.8f

                        scale = 0.4f

                    } else {
                        transitionX = 0f
                        transitionY = 0f
                        scale = 1f
                    }
                }
        ) {

            Row(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AnimatedVisibility(
                    visible = searchState.showState,
                    enter = fadeIn(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Text(
                        text = "Search in",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = stateString,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    overflow = TextOverflow.Visible
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.Center),
                visible = searchState.showState
            ) {
                BasicTextField2(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    value = searchState.searchText,
                    onValueChange = { onChangeSearchString(it) },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
            }
        }

    }
}

// this is Done. next Journey!!

@Composable
private fun topSearchBarButtons(
    modifier: Modifier,
    searchState: TopFilterBarSearchState,
    onClickUp: () -> Unit,
    onClickFilter: () -> Unit,
    onClickSearch: () -> Unit,
    onClickCancel: () -> Unit
) {

    Surface(
        modifier = modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TODO() ICON ANIMATION

            TopFilterBarArrowToLensAnimation(
                modifier = Modifier.size(32.dp),
                showSearch = searchState.showState,
                onClickLens = {

                    onClickSearch()
                },
                onClickArrow = {
                    onClickUp()

                }
            )

            TopFilterBarFilterToCross(
                modifier = Modifier.size(32.dp),
                showFilter = searchState.showState,
                onClickFilter = {

                    onClickFilter()
                },
                onClickCross = {

                    onClickCancel()
                }
            )

        }
    }

}


@Composable
private fun topFilterBar(
    modifier: Modifier = Modifier,
    selectedFilter: Int,
    state: TopFilterBarType,
    onChangeFilterType: (TopFilterBarType) -> Unit
) {
    val showIndent by remember(key1 = state) {
        mutableStateOf(state != TopFilterBarType.ReminderType(TopFilterBarReminderType.All))
    }

    RowSelectionBox(
        columnModifier = modifier,
        boxModifier = Modifier.height(60.dp),
        selectedIndex = selectedFilter,
        showIndent = showIndent,
        alignment = RowBoxSelectionShapeAlignment.BOTTOM,
        borderStroke = BorderStroke(0.dp, Color.Transparent),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        buttonsPadding = PaddingValues(),
        cornerRadius = 30.dp,
        buttons = {
            topFilterBarButtons(state = state, onChangeFilterBarType = { onChangeFilterType(it) })
        }
    ) {
        /* DO NOTHING */
    }

}

@Composable
private fun topFilterBarButtons(
    state: TopFilterBarType,
    onChangeFilterBarType: (TopFilterBarType) -> Unit
) {

    when (state) {
        is TopFilterBarType.JourneyType -> {

            TopFilterBarFilterAscending(
                modifier = Modifier.size(28.dp),
                onClick = {
                    onChangeFilterBarType(
                        TopFilterBarType.JourneyType(
                            TopFilterBarJourneyType.Ascending
                        )
                    )
                }
            )

            TopFilterBarFilterDescending(
                modifier = Modifier.size(28.dp),
                onClick = {
                    onChangeFilterBarType(
                        TopFilterBarType.JourneyType(
                            TopFilterBarJourneyType.Descending
                        )
                    )
                }
            )

        }

        is TopFilterBarType.ReminderType -> {

            ReminderAlertAnimation(
                modifier = Modifier.size(28.dp),
                onClick = {
                    onChangeFilterBarType(
                        TopFilterBarType.ReminderType(
                            TopFilterBarReminderType.Alert
                        )
                    )
                }
            )

            ReminderBirthdayAnimation(
                modifier = Modifier.size(28.dp),
                onClick = {
                    onChangeFilterBarType(
                        TopFilterBarType.ReminderType(
                            TopFilterBarReminderType.Birthday
                        )
                    )
                }
            )

            ReminderEventAnimation(
                modifier = Modifier.size(28.dp),
                onClick = {
                    onChangeFilterBarType(
                        TopFilterBarType.ReminderType(
                            TopFilterBarReminderType.Event
                        )
                    )
                }
            )

        }
    }

}




