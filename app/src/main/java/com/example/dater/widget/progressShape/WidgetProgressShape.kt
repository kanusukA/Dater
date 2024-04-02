package com.example.dater.widget.progressShape


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.unit.Dimension

@Composable
fun WidgetProgressBox(
    modifier: GlanceModifier,
    progress: Float,
    cornerRadius: Dimension.Dp = Dimension.Dp(20.dp),

    ){

    androidx.glance.layout.Box (
        modifier = modifier
    ){

    }

}

private fun progressPathLine(
    size: Size,
    cornerSize: Size = Size(160f, 160f),
    offset: Offset = Offset.Zero
): Path {
    val path = Path()

    path.moveTo(0f + offset.x , (size.height / 2f) + offset.y)

//    path.lineTo(0f , 0f + cornerSize.height )

    path.arcTo(
        rect = Rect(
            Offset(0f + offset.x , 0f + offset.y),
            cornerSize
        ),
        startAngleDegrees = -180f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.lineTo((size.width - cornerSize.width) + offset.x, 0f + offset.y)

    path.arcTo(
        rect = Rect(
            Offset((size.width - cornerSize.width) + offset.x, 0f + offset.y),
            cornerSize
        ),
        startAngleDegrees = -90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.lineTo(size.width + offset.x, (size.height - cornerSize.height) + offset.y )

    path.arcTo(
        rect = Rect(
            Offset(
                (size.width - cornerSize.width) + offset.x,
                (size.height - cornerSize.height) + offset.y
            ),
            cornerSize
        ),
        startAngleDegrees = 0f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.lineTo(cornerSize.width  + offset.x, size.height + offset.y)

    path.arcTo(
        rect = Rect(
            Offset(0f + offset.x, (size.height - cornerSize.height) + offset.y),
            cornerSize
        ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = 90f,
        forceMoveTo = false
    )

    path.lineTo(0f + offset.x, (size.height / 2f) + offset.y)


    return path
}


@Preview
@Composable
fun previewBox() {

    val cornerSize by remember {
        mutableStateOf(80f)
    }

    val density = LocalDensity.current

    var progress by remember {
        mutableStateOf(0f)
    }

    val color = MaterialTheme.colorScheme.secondary

    val ballColor = MaterialTheme.colorScheme.tertiaryContainer

    val backgroundColor = MaterialTheme.colorScheme.secondaryContainer

    val animatedLoop = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(3000),
        label = ""
    )


    Column() {
        Box(
            modifier = Modifier
                .size(200.dp, 100.dp)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                val outerCornerSize = (size.height / 4) + cornerSize

                val lineThickness = 40f

                val outerPath = progressPathLine(
                    size = Size(size.width - lineThickness,size.height - lineThickness),
                    cornerSize = Size(outerCornerSize, outerCornerSize),
                    offset = Offset(lineThickness / 2,lineThickness / 2)
                )

                //Background
                drawPath(outerPath,color = backgroundColor.copy(alpha = 0.7f), style = Stroke(lineThickness))

                val pathMeasure = PathMeasure()
                val destination = Path()

                pathMeasure.setPath(outerPath, false)
                val pathSegment = pathMeasure.length * animatedLoop.value
                pathMeasure.getSegment(0f, pathSegment, destination, true)

                drawPath(destination, color = color, style = Stroke(lineThickness))

                val radius = lineThickness / 2

                drawCircle(
                    color = color,
                    radius = radius,
                    center = Offset(radius, (size.height / 2))
                )

                drawCircle(
                    color = ballColor,
                    radius = radius,
                    center = pathMeasure.getPosition(pathSegment)
                )

            }
        }

        FilledTonalButton(onClick = {
            if (progress == 1f) {
                progress = 0f
            } else {
                progress = 1f
            }
        }) {
            Text(text = "Loop")
        }
    }
}

