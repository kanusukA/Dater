package com.example.dater.ui.components.SelectionBox.CollapseAnimation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

interface IndentAnimation {
    @Composable
    fun animationShapeAsState(
        targetOffset: Offset,
        showIndent: Boolean,
        cornerRadius: Dp
    ): State<Shape>
}