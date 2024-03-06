package dehnavi.sajjad.canvascompose.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star

@Composable
fun VerifyShape() {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedProgress = animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow)
    )
    val verifyPolygon = remember {
        RoundedPolygon.star(
            numVerticesPerRadius = 9,
            innerRadius = 2f / 3f,
            rounding = CornerRounding(1f / 8f)
        )
    }

    val circlePolygon = remember {
        RoundedPolygon.circle(
            numVertices = 12
        )
    }

    val morph = remember {
        Morph(verifyPolygon, circlePolygon)
    }
    var morphPath = remember {
        Path()
    }

    BoxWithConstraints(
        modifier = Modifier
            .padding(16.dp)
            .clickable(interactionSource = interactionSource, indication = null) {
            }
            .drawWithCache {
                morphPath = morph
                    .toComposePath(
                        progress = animatedProgress.value,
                        scale = size.minDimension / 2f,
                        path = morphPath
                    )

                onDrawBehind {
                    translate(size.width / 2f, size.height / 2f) {
                        drawPath(morphPath, color = Color.Blue)
                    }
                }
            }
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .size(this.maxWidth / 1.6f),
            tint = Color.White,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun VerifyShapePreview() {
    VerifyShape()
}