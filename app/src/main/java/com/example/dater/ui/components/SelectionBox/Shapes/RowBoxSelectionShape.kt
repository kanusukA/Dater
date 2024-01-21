package com.example.dater.ui.components.SelectionBox.Shapes

import android.graphics.PointF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

data class RowBoxSelectionShapeData(
    val cornerRadius: Dp = 20.dp,
    val itemPosition: Offset = Offset.Zero,
    val indentSize: Size = Size(80f, 80f),
    val alignment: RowBoxSelectionShapeAlignment,
    val showIndent: Boolean = true,
    val density: Density
)

enum class RowBoxSelectionShapeAlignment {
    TOP,
    BOTTOM
}

class RowBoxSelectionShape(
    private val rowBoxSelectionShapeData: RowBoxSelectionShapeData
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val roundedRectShape = roundedRectShape(
            size = size,
            cornerRadius = rowBoxSelectionShapeData.cornerRadius,
            density = density
        )
        var shape = roundedRectShape

        if (rowBoxSelectionShapeData.showIndent){
            val rowIndentPath = rowIndentPath(
                itemPosition = rowBoxSelectionShapeData.itemPosition,
                alignment = rowBoxSelectionShapeData.alignment,
                indentSize = rowBoxSelectionShapeData.indentSize,
                size = size
            )

            shape = Path.combine(
                PathOperation.Difference,
                roundedRectShape,
                rowIndentPath
            )
        }

        return Outline.Generic(shape)
    }

    fun copy(
        cornerRadius: Dp = rowBoxSelectionShapeData.cornerRadius,
        alignment: RowBoxSelectionShapeAlignment = rowBoxSelectionShapeData.alignment,
        indentSize: Size = rowBoxSelectionShapeData.indentSize,
        density: Density = rowBoxSelectionShapeData.density,
        showIndent: Boolean = rowBoxSelectionShapeData.showIndent,
        itemPosition: Offset = rowBoxSelectionShapeData.itemPosition
    ) = RowBoxSelectionShape(
        rowBoxSelectionShapeData = RowBoxSelectionShapeData(
            cornerRadius, itemPosition, indentSize, alignment, showIndent, density
        )
    )

}

private fun rowIndentPath(
    itemPosition: Offset,
    alignment: RowBoxSelectionShapeAlignment,
    indentSize: Size,
    size: Size
): Path {

    val itemOffset = if (alignment == RowBoxSelectionShapeAlignment.TOP) {
        Offset(itemPosition.x - (indentSize.width / 2f), 0f)
    } else {
        Offset(itemPosition.x - (indentSize.width / 2f), size.height)
    }

    val rect = Rect(
        offset = itemOffset,
        size = Size(indentSize.width, indentSize.height / 2f)
    )

    val maxX = 110f
    val maxY = 50f

    fun translate(x: Float, y: Float): PointF {
        return PointF(
            ((x / maxX) * rect.width) + rect.left,
            ((y / maxY) * rect.height) + rect.top
        )
    }

    val start = translate(x = 0f, y = 0f)
    var middle = translate(x = 55f, y = 34f)
    val end = translate(x = 110f, y = 0f)

    val control1 = translate(x = 23f, y = 0f)
    var control2 = translate(x = 39f, y = 34f)
    var control3 = translate(x = 71f, y = 34f)
    val control4 = translate(x = 87f, y = 0f)

    if (alignment == RowBoxSelectionShapeAlignment.BOTTOM) {

        middle = translate(x = 55f, y = -34f)

        control2 = translate(x = 39f, y = -34f)
        control3 = translate(x = 71f, y = -34f)
    }

    val path = Path()
    path.moveTo(start.x, start.y)
    path.cubicTo(control1.x, control1.y, control2.x, control2.y, middle.x, middle.y)
    path.cubicTo(control3.x, control3.y, control4.x, control4.y, end.x, end.y)

    return path
}