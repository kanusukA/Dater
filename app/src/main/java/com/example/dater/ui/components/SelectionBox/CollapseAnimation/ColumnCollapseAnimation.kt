package com.example.dater.ui.components.SelectionBox.CollapseAnimation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionAlignment
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionShape
import com.example.dater.ui.components.SelectionBox.Shapes.ColumnBoxSelectionShapeData


@Stable
class ColumnCollapseAnimation(
    private val animationSpec: AnimationSpec<Offset> = tween(600),
    private val alignment: ColumnBoxSelectionAlignment
): IndentAnimation {

    @Composable
    override fun animationShapeAsState(targetOffset: Offset, showIndent: Boolean, cornerRadius: Dp): State<Shape> {

        val density = LocalDensity.current

        val columnShape = ColumnBoxSelectionShape(
            columnBoxSelectionShapeData = ColumnBoxSelectionShapeData(
                alignment = alignment,
                density = density,
                cornerRadius = cornerRadius,
                showIndent = showIndent
            )
        )

        if (targetOffset.isSpecified){

            val indentPos = animateOffsetAsState(
                targetValue = targetOffset,
                label = "",
                animationSpec = animationSpec
            )

            return produceState(initialValue = columnShape,
                key1 = targetOffset,
                key2 = indentPos.value,
                key3 = showIndent
            ) {
                this.value = this.value.copy(
                    itemPosition = indentPos.value,
                    density = density,
                    showIndent = showIndent
                )
            }
        }else{
            return remember {
                mutableStateOf(columnShape)
            }
        }
    }

}