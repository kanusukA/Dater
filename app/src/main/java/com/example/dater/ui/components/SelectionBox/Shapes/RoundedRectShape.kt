package com.example.dater.ui.components.SelectionBox.Shapes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp


@Stable
fun Float.toDp(density: Density): Dp = with(density) {this@toDp.toDp()}

@Stable
fun Dp.toPxf(density: Density): Float = with(density) {this@toPxf.toPx()}

fun roundedRectShape(
    size: Size,
    cornerRadius: Dp,
    density: Density,
): Path {

    val cornerSize = Size(cornerRadius.toPxf(density) * 2, cornerRadius.toPxf(density) * 2)

    return if ((size.height - cornerSize.height) < 1){
        twoCorner(
            size
        )
    }else{
        fourCorner(
            size, cornerRadius, density
        )
    }
}

fun twoCorner(
    size: Size,
): Path{
    val cornerSize = Size(size.height,size.height)

    return Path().apply {

        moveTo(size.width / 2f, 0f)

        lineTo(size.width - cornerSize.width, 0f)

        arcTo(
            Rect(
                offset = Offset(size.width - cornerSize.width, 0f),
                size = cornerSize
            ),
            -90f, 180f,
            forceMoveTo = false
        )

        lineTo(cornerSize.width, size.height)

        arcTo(
            Rect(
                offset = Offset(0f, 0f),
                size = cornerSize
            ),
            90f,
            180f,
            forceMoveTo = false
        )

        lineTo(size.width / 2f, 0f)
    }
}

fun fourCorner(
    size: Size,
    cornerRadius: Dp,
    density: Density,
): Path{
    val cornerSize = Size(cornerRadius.toPxf(density) * 2, cornerRadius.toPxf(density) * 2)

    return Path().apply {

        moveTo(size.width / 2f, 0f)

        lineTo(size.width - cornerSize.width, 0f)

        arcTo(
            Rect(
                offset = Offset(size.width - cornerSize.width, 0f),
                size = cornerSize
            ),
            -90f, 90f,
            forceMoveTo = false
        )

        lineTo(size.width, size.height - cornerSize.height)

        arcTo(
            Rect(
                offset = Offset(size.width - cornerSize.width, size.height - cornerSize.height),
                size = cornerSize
            ),
            0f,
            90f,
            forceMoveTo = false
        )


        lineTo(cornerSize.width, size.height)

        arcTo(
            Rect(
                offset = Offset(0f, size.height - cornerSize.height),
                size = cornerSize
            ),
            90f,
            90f,
            forceMoveTo = false
        )

        lineTo(0f, cornerSize.height)

        arcTo(
            Rect(
                offset = Offset(0f, 0f),
                size = cornerSize
            ),
            180f, 90f,
            forceMoveTo = false
        )

        lineTo(size.width / 2f, 0f)
    }

}

@Preview
@Composable
fun previewTestingShape(){

}