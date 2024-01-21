package com.example.dater.ui.components.SelectionBox


import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.example.dater.ui.components.SelectionBox.CollapseAnimation.ColumnCollapseAnimation
import com.example.dater.ui.components.SelectionBox.CollapseAnimation.IndentAnimation
import com.example.dater.ui.components.SelectionBox.CollapseAnimation.RowCollapseAnimation
import com.example.dater.ui.components.SelectionBox.MeasurePolicy.columnBoxSelectionMeasurePolicy
import com.example.dater.ui.components.SelectionBox.MeasurePolicy.rowBoxSelectionMeasurePolicy
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionAlignment
import com.example.dater.ui.components.SelectionBox.Shapes.RowBoxSelectionShapeAlignment

@Composable
fun RowSelectionBox(
    modifier: Modifier = Modifier,
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
        modifier = Modifier
            .background(color = color, shape = shape.value)
            .border(borderStroke, shape = shape.value)
            .onGloballyPositioned {
                parentWidth = it.size.width
            }
    ) {
        if (alignment == RowBoxSelectionShapeAlignment.BOTTOM) {
            Box(
                modifier = modifier,
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
                modifier = modifier,
            ) { content() }
        }

    }

}
/* TODO BUTTON PADDING NOT WORKING */
@Composable
fun ColumnSelectionBox(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceEvenly,
    alignment: ColumnBoxSelectionAlignment = ColumnBoxSelectionAlignment.START,
    indentAnimation: IndentAnimation = ColumnCollapseAnimation(tween(600), alignment),
    selectedIndex: Int,
    cornerRadius: Dp = 20.dp,
    color: Color = Color.Transparent,
    showIndent: Boolean,
    borderStroke: BorderStroke = BorderStroke(3.dp, MaterialTheme.colorScheme.outlineVariant),
    buttonsPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    onClick: () -> Unit = {},
    buttons: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    var parentHeight by remember {
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


    Surface(
        shape = shape.value,
        color = color,
        border = borderStroke,
        onClick = {onClick()},
        modifier = modifier,
    ){
        Row() {
            if (alignment == ColumnBoxSelectionAlignment.END) {
                Box(modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned {
                        parentHeight = it.size.height
                    }
                ) {
                    content()
                }
            }
            Layout(
                modifier = Modifier

                    .padding(buttonsPadding),
                content = buttons,
                measurePolicy = MeasurePolicy { measurables, constraints ->

                    val minItemSizePx = 0
                    val maxItemSizePx = 48.dp.roundToPx()

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

                    val totalHeight = placeables.sumOf { it.height }

                    // Layout Size
                    val layoutHeight = if (totalHeight < parentHeight) {
                        parentHeight
                    } else {
                        totalHeight
                    }
                    val layoutWidth = if (placeables.isNotEmpty()) {
                        placeables.maxOf { it.width }
                    } else {
                        0
                    }

                    // Arrangement Variables
                    var topPadding = 0
                    var incrementalPadding = 0
                    val arrangementHeight = layoutHeight - totalHeight

                    when (arrangement) {

                        Arrangement.Center -> {
                            topPadding = arrangementHeight / 2
                        }

                        Arrangement.SpaceEvenly -> {
                            topPadding = arrangementHeight / placeables.size + 1
                            incrementalPadding = arrangementHeight / placeables.size + 1
                        }
                    }


                    layout(layoutWidth, layoutHeight) {

                        // Arrangement Filter

                        var yPos = topPadding
                        var xPos = 0

                        val positions = arrayListOf<Float>()



                        placeables.map { placeable ->

                            placeable.placeRelative(x = xPos, y = yPos)

                            positions.add(yPos + (placeable.height / 2f) + ((parentHeight - layoutHeight) / 2f))
                            yPos += incrementalPadding + placeable.height
                        }
                        itemPosition = positions.map { y ->
                            Offset(0f, y)
                        }

                    }

                }
            )

            if (alignment == ColumnBoxSelectionAlignment.START) {
                Box(modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned {
                        parentHeight = it.size.height
                    }
                ) {
                    content()
                }
            }


        }
    }
}



