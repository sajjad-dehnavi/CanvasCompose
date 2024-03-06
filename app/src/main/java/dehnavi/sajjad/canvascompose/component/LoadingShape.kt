package dehnavi.sajjad.canvascompose.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.flatten
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoadingShape(
    modifier: Modifier = Modifier
) {
    val starPolygon = remember {
        RoundedPolygon.star(
            numVerticesPerRadius = 13,
            innerRadius = 2f / 3f,
            rounding = CornerRounding(1f / 8f)
        )
    }
    val loadingMorph = remember {
        Morph(starPolygon, starPolygon)
    }

    var loadingPath = remember {
        Path()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val progress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    val pathMeasurer = remember {
        PathMeasure()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .drawWithCache {
                loadingPath = loadingMorph
                    .toComposePath(
                        progress = progress.value,
                        scale = size.minDimension / 2f,
                        path = loadingPath
                    )


                val loadingAndroidPath = loadingPath.asAndroidPath()
                val flattenedLoadingPath = loadingAndroidPath.flatten()

                pathMeasurer.setPath(loadingPath, false)
                val totalLength = pathMeasurer.length


                onDrawBehind {
                    rotate(rotation.value) {
                        translate(size.width / 2f, size.height / 2f) {
                            val currentLength = totalLength * progress.value
                            flattenedLoadingPath.forEach { line ->
                                if (line.startFraction * totalLength < currentLength) {
                                    val brush =
                                        Brush.sweepGradient(colors, center = Offset(0.5f, 0.5f))
                                    if (progress.value > line.endFraction) {
                                        drawLine(
                                            brush = brush,
                                            start = Offset(line.start.x, line.start.y),
                                            end = Offset(line.end.x, line.end.y),
                                            strokeWidth = 16.dp.toPx(),
                                            cap = StrokeCap.Round
                                        )
                                    } else {
                                        val endX = mapValue(
                                            progress.value,
                                            line.startFraction,
                                            line.endFraction,
                                            line.start.x,
                                            line.end.x
                                        )
                                        val endY = mapValue(
                                            progress.value,
                                            line.startFraction,
                                            line.endFraction,
                                            line.start.y,
                                            line.end.y
                                        )
                                        drawLine(
                                            brush = brush,
                                            start = Offset(line.start.x, line.start.y),
                                            end = Offset(endX, endY),
                                            strokeWidth = 16.dp.toPx(),
                                            cap = StrokeCap.Round
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
    )
}


private fun mapValue(
    value: Float,
    fromRangeStart: Float,
    fromRangeEnd: Float,
    toRangeStart: Float,
    toRangeEnd: Float
): Float {
    val ratio =
        (value - fromRangeStart) / (fromRangeEnd - fromRangeStart)
    return toRangeStart + ratio * (toRangeEnd - toRangeStart)
}

fun Morph.toComposePath(progress: Float, scale: Float = 1f, path: Path = Path()): Path {
    var first = true
    path.rewind()
    forEachCubic(progress) { bezier ->
        if (first) {
            path.moveTo(bezier.anchor0X * scale, bezier.anchor0Y * scale)
            first = false
        }
        path.cubicTo(
            bezier.control0X * scale, bezier.control0Y * scale,
            bezier.control1X * scale, bezier.control1Y * scale,
            bezier.anchor1X * scale, bezier.anchor1Y * scale
        )
    }
    path.close()
    return path
}

private val colors = listOf(
    Color(0xFFBD4CE0),
    Color(0xFF673AB7),
    Color(0xFFBC96FF),
    Color(0xFF2196F3),
    Color(0xFF35C6FF),
    Color(0xFF9BA8FF),
    Color(0xFFE696FF),
    Color(0xFFBD4CE0),
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewShape() {
    LoadingShape()
}