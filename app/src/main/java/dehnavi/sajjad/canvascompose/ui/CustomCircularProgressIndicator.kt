package dehnavi.sajjad.canvascompose.ui

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dehnavi.sajjad.canvascompose.ui.theme.darkGray
import dehnavi.sajjad.canvascompose.ui.theme.gray
import dehnavi.sajjad.canvascompose.ui.theme.orange
import dehnavi.sajjad.canvascompose.ui.theme.white
import java.lang.Math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    initialValue: Int = 0,
    primaryColor: Color,
    secondaryColor: Color,
    minValue: Int = 0,
    maxValue: Int = 100,
    circleRadius: Float,
    onPositionChange: (Int) -> Unit,
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var positionValue by remember {
        mutableStateOf(initialValue)
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val circleThickness = width / 30f
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        primaryColor.copy(0.45f),
                        secondaryColor.copy(0.15f)
                    ),
                ), center = circleCenter, radius = circleRadius
            )

            drawCircle(
                color = secondaryColor,
                center = circleCenter,
                radius = circleRadius,
                style = Stroke(width = circleThickness)
            )

            drawArc(
                color = primaryColor,
                startAngle = 90f,
                sweepAngle = (360f / maxValue) * positionValue.toFloat(),
                useCenter = false,
                size = Size(width = circleRadius * 2f, height = circleRadius * 2f),
                style = Stroke(
                    width = circleThickness,
                    cap = StrokeCap.Round
                ),
                topLeft = Offset(
                    x = (width - circleRadius * 2f) / 2f,
                    y = (height - circleRadius * 2f) / 2f,
                )
            )

            val otherRadius = circleRadius + circleThickness / 2f
            val gap = 15f
            for (i in 1..maxValue - minValue) {
                val color =
                    if (i <= positionValue - minValue) primaryColor else primaryColor.copy(alpha = 0.3f)
                val angleDegrees = i * 360f / (maxValue - minValue).toFloat()
                val angleRad = angleDegrees * PI / 180f + PI / 2f
                val yGapAdjust = cos(angleDegrees * PI / 180) * gap
                val xGapAdjust = -sin(angleDegrees * PI / 180) * gap

                val start = Offset(
                    x = (otherRadius * cos(angleRad) + circleCenter.x + xGapAdjust).toFloat(),
                    y = (otherRadius * sin(angleRad) + circleCenter.y + yGapAdjust).toFloat()
                )

                val end = Offset(
                    x = (otherRadius * cos(angleRad) + circleCenter.x + xGapAdjust).toFloat(),
                    y = (otherRadius * sin(angleRad) + circleThickness + circleCenter.y + yGapAdjust).toFloat()
                )

                rotate(
                    angleDegrees,
                    pivot = start
                ) {
                    drawLine(
                        color = color,
                        start = start,
                        end = end,
                        strokeWidth = 1.dp.toPx(),
                    )
                }
            }
            drawContext.canvas.nativeCanvas.apply {
                drawIntoCanvas {
                    drawText(
                        "$positionValue %",
                        circleCenter.x,
                        circleCenter.y + 45.dp.toPx() / 3f,
                        Paint().apply {
                            textSize = 38.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            color = white.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewIndicator() {
    CustomCircularProgressIndicator(
        modifier = Modifier
            .background(darkGray)
            .size(250.dp),
        primaryColor = orange,
        secondaryColor = gray,
        circleRadius = 250f,
        initialValue = 55,
        onPositionChange = {}
    )
}