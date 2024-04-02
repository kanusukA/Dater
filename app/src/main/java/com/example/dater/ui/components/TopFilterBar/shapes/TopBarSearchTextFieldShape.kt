package com.example.dater.ui.components.TopFilterBar.shapes


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.example.dater.ui.components.SelectionBox.Shapes.toPxf

data class TopBarSearchTextFieldInputs(
    val gapLength: Dp,
    val gapHeight: Dp,
    val paddingStart: Dp,
    val cornerRadius: Dp
)
class TopBarSearchTextFieldShape (
    private val inputs: TopBarSearchTextFieldInputs
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            daterTextFieldPath(
                size = size,
                gapLength = inputs.gapLength.toPxf(density),
                gapHeight = inputs.gapHeight.toPxf(density),
                paddingStart = inputs.paddingStart.toPxf(density),
                cornerRadius = inputs.cornerRadius.toPxf(density)
            )
        )
    }
}

private fun topFilterBarSearchTextFieldPath(

    size: Size,
    cornerRadius: Dp,
    textGapLength: Dp,
    textHeight: Dp,
    paddingFromStart: Dp,
    density: Density

): Path {
    val path = Path()
    val cornerRadiusFloat = cornerRadius.toPxf(density)
    val textLength = textGapLength.toPxf(density)
    val textHeightFloat = textHeight.toPxf(density)
    val padding = paddingFromStart.toPxf(density)

    path.moveTo(0f, size.height / 2)

    // Start line
    if (cornerRadiusFloat < size.height / 2) {
        path.lineTo(0f, cornerRadiusFloat / 2)
    }

    //Top Left Curve
    path.arcTo(
        Rect(
            offset = Offset.Zero,
            size = Size(cornerRadiusFloat, cornerRadiusFloat)
        ),
        startAngleDegrees = -180f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    // Line to Text Box
    if (cornerRadiusFloat < padding) {
        path.lineTo(padding, 0f)

        //TextBox TopLeftCurve
        path.arcTo(
            rect = Rect(
                offset = Offset(padding, 0f),
                size = Size(cornerRadiusFloat, cornerRadiusFloat)
            ),
            startAngleDegrees = -90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        //line to Bottom Curve
        if (textHeightFloat > cornerRadiusFloat) {
            path.lineTo(cornerRadiusFloat + padding, textHeightFloat - (cornerRadiusFloat / 2))
        }

        //Bottom Left Curve
        path.arcTo(
            rect = Rect(
                offset = Offset(cornerRadiusFloat + padding, textHeightFloat - cornerRadiusFloat),
                size = Size(cornerRadiusFloat, cornerRadiusFloat)
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = -90f,
            forceMoveTo = false
        )
    } else {
        //TextBox TopLeftCurve
        path.arcTo(
            rect = Rect(
                offset = Offset(0f, 0f),
                size = Size(cornerRadiusFloat, cornerRadiusFloat)
            ),
            startAngleDegrees = -90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        //line to Bottom Curve
        if (textHeightFloat > cornerRadiusFloat) {
            path.lineTo(cornerRadiusFloat, textHeightFloat - (cornerRadiusFloat / 2))
        }

        //Bottom Left Curve
        path.arcTo(
            rect = Rect(
                offset = Offset(cornerRadiusFloat, textHeightFloat - cornerRadiusFloat),
                size = Size(cornerRadiusFloat, cornerRadiusFloat)
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = -90f,
            forceMoveTo = false
        )
    }





    // line to Bottom Right Curve
    if (textLength > cornerRadiusFloat) {
        path.lineTo(textLength + padding, textHeightFloat)
    }

    // Bottom Right Curve
    path.arcTo(
        rect = Rect(
            offset = Offset(
                (textLength + padding) - (cornerRadiusFloat / 2),
                textHeightFloat - cornerRadiusFloat
            ),
            size = Size(cornerRadiusFloat, cornerRadiusFloat)
        ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )

    //line to Top Right Curve
    if( textHeightFloat > cornerRadiusFloat){
        path.lineTo(textLength + padding + (cornerRadiusFloat/2),cornerRadiusFloat/2)
    }

    //Top Right curve
    path.arcTo(
        rect = Rect(
            offset = Offset(textLength + padding + (cornerRadiusFloat/2),0f),
            size = Size(cornerRadiusFloat,cornerRadiusFloat)
        ),
        startAngleDegrees = 180f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    // line to Rect end
    if(size.width - (textLength+padding) > cornerRadiusFloat){
        path.lineTo(size.width - (cornerRadiusFloat / 2),0f)
    }

    path.arcTo(
        rect = Rect(
            offset = Offset((size.width - cornerRadiusFloat),0f),
            size = Size(cornerRadiusFloat,cornerRadiusFloat)
        ),
        startAngleDegrees = -90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    if( size.width > cornerRadiusFloat){
        path.lineTo(size.width,size.height - (cornerRadiusFloat / 2))
    }

    path.arcTo(
        rect = Rect(
            offset = Offset(size.width - cornerRadiusFloat, size.height - cornerRadiusFloat),
            size = Size(cornerRadiusFloat,cornerRadiusFloat)
        ),
        startAngleDegrees = 0f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.lineTo(cornerRadiusFloat/2,size.height)

    path.arcTo(
        rect = Rect(
            offset = Offset(0f,size.height - cornerRadiusFloat),
            size = Size(cornerRadiusFloat,cornerRadiusFloat)
        ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.lineTo(0f,size.height/2)



    return path

}

private fun daterTextFieldPath(
    size: Size,
    cornerRadius: Float,
    gapHeight: Float,
    gapLength:Float,
    paddingStart: Float
)
: Path {
    val path = Path()

    val cornerSize = Size(cornerRadius,cornerRadius)

    path.moveTo(0f,size.height/2)

    path.arcTo(
        Rect(
            offset = Offset(0f,0f),
            size = cornerSize
        ),
        startAngleDegrees = 180f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.arcTo(
        Rect(
            offset = Offset(paddingStart,0f),
            size = cornerSize
        ),
        startAngleDegrees = -90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.arcTo(
        Rect(
            offset = Offset(paddingStart + cornerRadius,gapHeight),
            size = cornerSize
        ),
        startAngleDegrees = 180f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )

    path.arcTo(
        Rect(
            offset = Offset(paddingStart+cornerRadius+gapLength,gapHeight),
            size = cornerSize
        ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = -90f,
        forceMoveTo = false
    )

    path.arcTo(
        Rect(
            offset = Offset(paddingStart+(cornerRadius * 2)+gapLength,0f),
            size = cornerSize
        ),
        startAngleDegrees = 180f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.arcTo(
        Rect(
            offset = Offset(size.width - cornerRadius,0f),
            size = cornerSize
        ),
        startAngleDegrees = -90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.arcTo(
        Rect(
            offset = Offset(size.width - cornerRadius, size.height - cornerRadius),
            size = cornerSize
        ),
        startAngleDegrees = 0f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.arcTo(
        Rect(
            offset = Offset(0f,size.height - cornerRadius),
            size = cornerSize
        ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.lineTo(0f,size.height/2)

    return path
}

//@Preview
//@Composable
//fun previewTextField() {
//
//    // Create smoother curves
//
//    val density = LocalDensity.current
//
//    Canvas(
//        modifier = Modifier
//            .size(200.dp, 100.dp)
//            .padding(6.dp)
//    ) {
//        drawPath(
//            path = testPath(size,20.dp.toPx(),0.dp.toPx(), gapLength = 80.dp.toPx(), paddingStart = 6.dp.toPx()),
//            color = Color.Cyan,
//            style = Stroke(2f)
//        )
//
//
//    }
//}