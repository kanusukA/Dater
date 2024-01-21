package com.example.dater.ui.components.SelectionBox.MeasurePolicy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.isUnspecified

fun rowBoxSelectionMeasurePolicy(
    itemPositions: (ArrayList<Float>) -> Unit,
    itemArrangement: Arrangement.HorizontalOrVertical,
    minItemSize: Dp = 0.dp,
    maxItemSize: Dp = 32.dp,
    width: Dp = Dp.Unspecified
) = MeasurePolicy {
    measurables, constraints ->

    val minItemSizePx = minItemSize.roundToPx()
    val maxItemSizePx = maxItemSize.roundToPx()

    // Placeables - with max Size constraints
    val placeables = measurables.map {
        measurable ->
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

    // Layout Size
    val layoutHeight = placeables.maxOf { it.height }
    var layoutWidth = placeables.sumOf { it.width }


    // Arrangement Filter
    if (width.isSpecified){

        val totalWidth = width.roundToPx() - layoutWidth
        layoutWidth = width.roundToPx()

        when (itemArrangement) {

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
    }

    layout(layoutWidth,layoutHeight){

        var xPos = startPadding
        val positions = arrayListOf<Float>()

        placeables.forEach {

            placeable ->
            placeable.placeRelative(x = xPos, y = 0)
            positions.add(xPos + (placeable.width / 2f))

            xPos += incrementalPadding + placeable.width
        }

        itemPositions(positions)
    }

}