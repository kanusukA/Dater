package com.example.dater.ui.components.SelectionBox.Shapes

import android.graphics.PointF
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

data class ColumnBoxSelectionShapeData(
    val itemPosition: Offset = Offset.Zero,
    val cornerRadius: Dp = 20.dp,
    val indentSize: Size = Size(80f, 80f),
    val alignment: ColumnBoxSelectionAlignment,
    val showIndent: Boolean = true,
    val density: Density
)

enum class ColumnBoxSelectionAlignment {
    START,
    END
}

class ColumnBoxSelectionShape(
    private val columnBoxSelectionShapeData: ColumnBoxSelectionShapeData
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val roundedRectShape = roundedRectShape(
            size = size,
            cornerRadius = columnBoxSelectionShapeData.cornerRadius,
            density = density
        )

        var shape = roundedRectShape

        if(columnBoxSelectionShapeData.showIndent){
            val indentPath = columnIndentPath(
                itemPosition = columnBoxSelectionShapeData.itemPosition,
                alignment = columnBoxSelectionShapeData.alignment,
                indentSize = columnBoxSelectionShapeData.indentSize,
                size = size
            )

            shape = Path.combine(
                PathOperation.Difference,
                roundedRectShape,
                indentPath
            )
        }
        return Outline.Generic(shape)
    }

    fun copy(
        cornerRadius: Dp = columnBoxSelectionShapeData.cornerRadius,
        indentSize: Size = columnBoxSelectionShapeData.indentSize,
        itemPosition: Offset = columnBoxSelectionShapeData.itemPosition,
        alignment: ColumnBoxSelectionAlignment = columnBoxSelectionShapeData.alignment,
        showIndent: Boolean = columnBoxSelectionShapeData.showIndent,
        density: Density
    ) = ColumnBoxSelectionShape(
        columnBoxSelectionShapeData = ColumnBoxSelectionShapeData(
            itemPosition, cornerRadius, indentSize, alignment, showIndent, density,
        )
    )

}


private fun columnIndentPath(
    itemPosition: Offset,
    alignment: ColumnBoxSelectionAlignment,
    indentSize: Size,
    size: Size
): Path {


    val indentOffset = if (alignment == ColumnBoxSelectionAlignment.START) {
        Offset(0f, itemPosition.y - (indentSize.height / 2f))
    } else {
        Offset(size.width, itemPosition.y - (indentSize.height / 2f))
    }

    val rect = Rect(
        offset = indentOffset,
        size = Size(indentSize.width / 2f, indentSize.height)
    )

    val maxX = 55f
    val maxY = 110f

    fun translate(x: Float, y: Float): PointF {
        return PointF(
            ((x / maxX) * rect.width) + rect.left,
            ((y / maxY) * rect.height) + rect.top
        )
    }

    var start = translate(x = 0f, y = 0f)
    var middle = translate(x = 34f, y = 55f)
    var end = translate(x = 0f, y = 110f)

    var control1 = translate(x = 0f, y = 27f)
    var control2 = translate(x = 34f, y = 40f)
    var control3 = translate(x = 34f, y = 71f)
    var control4 = translate(x = 0f, y = 83f)

    if (alignment == ColumnBoxSelectionAlignment.END) {
        start = translate(x = 0f, y = 0f)
        middle = translate(x = -34f, y = 55f)
        end = translate(x = 0f, y = 110f)

        control1 = translate(x = 0f, y = 27f)
        control2 = translate(x = -34f, y = 40f)
        control3 = translate(x = -34f, y = 71f)
        control4 = translate(x = 0f, y = 83f)
    }


    val path = Path()
    path.moveTo(start.x, start.y)
    path.cubicTo(control1.x, control1.y, control2.x, control2.y, middle.x, middle.y)
    path.cubicTo(control3.x, control3.y, control4.x, control4.y, end.x, end.y)

    return path
}