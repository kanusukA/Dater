package com.example.dater.ui.components.SelectionBox.MeasurePolicy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.isUnspecified
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionAlignment

fun columnBoxSelectionMeasurePolicy(
    itemPositions: (ArrayList<Float>) -> Unit,
    itemArrangement: Arrangement.HorizontalOrVertical,
    minItemSize: Dp = 0.dp,
    maxItemSize: Dp = 32.dp,
    parentHeight: Int,
    alignment: ColumnBoxSelectionAlignment,
) = MeasurePolicy { measurables, constraints ->

    val minItemSizePx = minItemSize.roundToPx()
    val maxItemSizePx = maxItemSize.roundToPx()

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


    
    // Layout Size
    var layoutHeight = if (placeables.sumOf { it.height } < parentHeight) {parentHeight} else{placeables.sumOf { it.height }}
    val layoutWidth = if (placeables.isNotEmpty()){ placeables.maxOf { it.width } } else {0}


    // Arrangement Variables
    var topPadding = 0
    var incrementalPadding = 0


    layout(layoutWidth,layoutHeight){

        // Arrangement Filter

        val totalHeight = layoutHeight

        when (itemArrangement){

            Arrangement.SpaceEvenly -> {
                incrementalPadding = totalHeight / (placeables.size + 1)
            }

            Arrangement.SpaceBetween -> {
                incrementalPadding = totalHeight / (placeables.size - 1)
            }

            Arrangement.Bottom -> {
                topPadding = totalHeight
            }

            Arrangement.Center -> {
                topPadding = (totalHeight - placeables.sumOf { it.height }) / 2
            }

            else -> {
                //Do nothing for Arrangement.Top
            }
        }

        //Debugging
        var changeInHeight = 0

        var yPos = topPadding - incrementalPadding
        var xPos = 0
        val positions = arrayListOf<Float>()

        if(alignment == ColumnBoxSelectionAlignment.END){
            xPos = -(layoutWidth)
        }
        if(changeInHeight != layoutHeight){

            changeInHeight = layoutHeight
        }

        placeables.map {placeable ->

            placeable.placeRelative(x = xPos, y = yPos)


            yPos += incrementalPadding + placeable.height
        }

        itemPositions(positions)
    }

}