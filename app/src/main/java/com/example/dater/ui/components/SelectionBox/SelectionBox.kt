package com.example.dater.ui.components.SelectionBox


import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dater.ui.components.SelectionBox.CollapseAnimation.ColumnCollapseAnimation
import com.example.dater.ui.components.SelectionBox.CollapseAnimation.IndentAnimation
import com.example.dater.ui.components.SelectionBox.CollapseAnimation.RowCollapseAnimation
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionAlignment
import com.example.dater.ui.components.SelectionBox.Shapes.RowBoxSelectionShapeAlignment

@Composable
fun RowSelectionBox(
    columnModifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    arrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceEvenly,
    alignment: RowBoxSelectionShapeAlignment = RowBoxSelectionShapeAlignment.TOP,
    indentAnimation: IndentAnimation = RowCollapseAnimation(alignment = alignment),
    selectedIndex: Int,
    cornerRadius: Dp = 20.dp,
    color: Color = Color.Transparent,
    showIndent: Boolean,
    maxButtonSize: Dp = 48.dp,
    borderStroke: BorderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
    buttonsPadding: PaddingValues = PaddingValues(horizontal = 6.dp),
    buttons: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    var parentWidth by remember {
        mutableStateOf(0)
    }

    var itemPosition by remember {
        mutableStateOf(listOf<Offset>())
    }

    val selectedItemOffset by remember(selectedIndex, itemPosition) {
        derivedStateOf {
            try {
                itemPosition[selectedIndex]
            } catch (_: Exception) {
                Offset.Unspecified
            }
        }
    }

    val shape = indentAnimation.animationShapeAsState(
        targetOffset = selectedItemOffset,
        cornerRadius = cornerRadius,
        showIndent = showIndent
    )


    Column(
        modifier = columnModifier
            .background(color = color, shape = shape.value)
            .border(borderStroke, shape = shape.value)
            .onGloballyPositioned {
                parentWidth = it.size.width
            }
    ) {
        if (alignment == RowBoxSelectionShapeAlignment.BOTTOM) {
            Box(
                modifier = boxModifier,
            ) { content() }
        }

        Layout(
            modifier = Modifier.padding(paddingValues = buttonsPadding),
            content = buttons,
            measurePolicy = MeasurePolicy { measurables, constraints ->

                val minItemSizePx = 0
                val maxItemSizePx = maxButtonSize.roundToPx()

                // Placeables - with max Size constraints
                val placeables = measurables.map { measurable ->
                    measurable.measure(
                        Constraints(
                            minWidth = minItemSizePx,
                            maxWidth = maxItemSizePx,
                            minHeight = minItemSizePx,
                            maxHeight = maxItemSizePx
                        )
                    )
                }


                // Arrangement Variables
                var startPadding = 0
                var incrementalPadding = 0

                val itemTotalWidth = placeables.sumOf { it.width }

                // Layout Size
                val layoutHeight = placeables.maxOf { it.height }
                val layoutWidth = if (itemTotalWidth < parentWidth) {
                    parentWidth
                } else {
                    itemTotalWidth
                }


                // Arrangement Filter


                val totalWidth = layoutWidth - itemTotalWidth

                when (arrangement) {

                    Arrangement.SpaceEvenly -> {
                        startPadding = totalWidth / (placeables.size + 1)
                        incrementalPadding = totalWidth / (placeables.size + 1)

                    }

                    Arrangement.SpaceBetween -> {
                        incrementalPadding = totalWidth / (placeables.size - 1)
                    }

                    Arrangement.End -> {
                        startPadding = totalWidth
                    }

                    Arrangement.Center -> {
                        startPadding = totalWidth / 2
                    }

                    else -> {
                        // Do Nothing for Arrangement.Start
                    }
                }


                layout(layoutWidth, layoutHeight) {

                    var xPos = startPadding
                    val positions = arrayListOf<Float>()

                    placeables.forEach {

                            placeable ->
                        placeable.placeRelative(x = xPos, y = 0)
                        positions.add(xPos + (placeable.width / 2f))

                        xPos += incrementalPadding + placeable.width
                    }

                    itemPosition = positions.map {
                        Offset(it, 0f)
                    }

                }

            }
        )

        if (alignment == RowBoxSelectionShapeAlignment.TOP) {
            Box(
                modifier = boxModifier,
            ) { content() }
        }

    }

}

/* TODO BUTTON PADDING NOT WORKING */
@Composable
fun ColumnSelectionBox(
    rowModifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    arrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceEvenly,
    alignment: ColumnBoxSelectionAlignment = ColumnBoxSelectionAlignment.START,
    indentAnimation: IndentAnimation = ColumnCollapseAnimation(tween(600), alignment),
    selectedIndex: Int,
    cornerRadius: Dp = 20.dp,
    color: Color = Color.Transparent,
    showIndent: Boolean,
    borderStroke: BorderStroke = BorderStroke(3.dp, MaterialTheme.colorScheme.outlineVariant),
    contentAlignment: Alignment = Alignment.TopStart,
    maxButtonSize: Dp = 48.dp,
    minButtonSize: Dp = 0.dp,
    buttonSpacing: PaddingValues = PaddingValues(),
    onClick: () -> Unit = {},
    buttons: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var parentHeight by remember {
        mutableIntStateOf(0)
    }

    var itemPosition by remember {
        mutableStateOf(listOf<Offset>())
    }

    val selectedItemOffset by remember(selectedIndex, itemPosition) {
        derivedStateOf {
            try {
                itemPosition[selectedIndex]
            } catch (_: Exception) {

                Offset.Unspecified
            }
        }
    }

    val shape = indentAnimation.animationShapeAsState(
        targetOffset = selectedItemOffset,
        cornerRadius = cornerRadius,
        showIndent = showIndent
    )



    Row(
        modifier =
        rowModifier
            .background(color = color, shape = shape.value)
            .border(borderStroke, shape = shape.value)
            .clickable(interactionSource,indication = null){
                onClick()
            }
    ) {
        if (alignment == ColumnBoxSelectionAlignment.END) {
            Box(
                modifier = boxModifier
                    .weight(1f)
                    .onGloballyPositioned {
                        parentHeight = it.size.height
                    },
                contentAlignment = contentAlignment
            ) {
                content()
            }
        }
        Layout(
            modifier = Modifier,
            content = buttons,
            measurePolicy = MeasurePolicy { measurables, constraints ->

                val minButtonSize = minButtonSize.roundToPx()
                val maxButtonSize = maxButtonSize.roundToPx()

                val topButtonPadding = buttonSpacing.calculateTopPadding().roundToPx()
                val bottomButtonPadding = buttonSpacing.calculateBottomPadding().roundToPx()
                val startButtonPadding =
                    buttonSpacing.calculateStartPadding(layoutDirection).roundToPx()
                val endButtonPadding =
                    buttonSpacing.calculateEndPadding(layoutDirection).roundToPx()


                // Button Measurement
                val buttonPlaceables = measurables.map {
                    it.measure(
                        Constraints(
                            minWidth = minButtonSize,
                            maxWidth = maxButtonSize,
                            minHeight = minButtonSize,
                            maxHeight = maxButtonSize
                        )
                    )
                }

                val items = buttonPlaceables.size


                // Layout Calculation
                val totalButtonHeight = buttonPlaceables.sumOf { it.height }

                val arrangementHeight = if (totalButtonHeight > parentHeight) {
                    totalButtonHeight
                } else {
                    parentHeight
                }
                val arrangementWidth = buttonPlaceables.maxOf { it.width }


                val totalButtonHeightPadding =
                    totalButtonHeight + (topButtonPadding * items) + (bottomButtonPadding * items)

                val layoutHeight = if (totalButtonHeightPadding > parentHeight) {
                    totalButtonHeightPadding
                } else {
                    parentHeight
                }
                val layoutWidth =
                    buttonPlaceables.maxOf { it.width } + startButtonPadding + endButtonPadding

                var topPadding = 0
                var incrementalPadding = 0
                var spacing = 0


                // Button Arrangement
                when (arrangement) {

                    Arrangement.SpaceEvenly -> {

                        spacing = (arrangementHeight - totalButtonHeight) / (items + 1)

                        topPadding = spacing
                        incrementalPadding = spacing


                    }

                    Arrangement.SpaceBetween -> {

                        spacing = (layoutHeight - totalButtonHeight) / (items - 1)

                        topPadding = 0
                        incrementalPadding = spacing

                    }

                    Arrangement.Top -> {
                        /* Do Nothing */
                    }

                    Arrangement.Center -> {

                        spacing = (arrangementHeight - totalButtonHeight) / 2

                        topPadding = spacing
                        incrementalPadding = 0

                    }

                    Arrangement.Bottom -> {

                        spacing = (arrangementHeight - totalButtonHeight)

                        topPadding = spacing
                        incrementalPadding = 0

                    }

                    else -> {
                        /* Do Nothing */
                    }

                }


                var _itemPos = mutableListOf<Offset>()

                layout(layoutWidth, layoutHeight) {

                    topPadding += topButtonPadding
                    var yPos = topPadding

                    buttonPlaceables.forEach { placeable ->

                        placeable.place(x = startButtonPadding, y = yPos)

                        _itemPos.add(Offset(0f, (yPos + (placeable.height / 2)).toFloat()))
                        yPos += (incrementalPadding + placeable.height) + topButtonPadding + bottomButtonPadding


                    }
                    //Test
                    itemPosition = _itemPos.toList()


                }

            }
        )

        if (alignment == ColumnBoxSelectionAlignment.START) {
            Box(
                modifier = boxModifier
                    .weight(1f)
                    .onGloballyPositioned {
                        parentHeight = it.size.height
                    },
                contentAlignment = contentAlignment
            ) {
                content()
            }
        }


    }


}



