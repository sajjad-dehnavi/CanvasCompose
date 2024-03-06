package dehnavi.sajjad.canvascompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import dehnavi.sajjad.canvascompose.component.CustomCircularProgressIndicator
import dehnavi.sajjad.canvascompose.ui.theme.CanvasComposeTheme
import dehnavi.sajjad.canvascompose.ui.theme.darkGray
import dehnavi.sajjad.canvascompose.ui.theme.gray
import dehnavi.sajjad.canvascompose.ui.theme.orange
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Crossfade(targetState = true, label = "") {
                when (it) {
                    true -> Text(text = "")
                    else -> {}
                }
            }
            CanvasComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(darkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomCircularProgressIndicator(
                            modifier = Modifier
                                .size(250.dp)
                                .background(darkGray),
                            initialValue = 50,
                            primaryColor = orange,
                            secondaryColor = gray,
                            circleRadius = 230f,
                            onPositionChange = { position ->
                                //do something with this position value
                                Log.d("TAG", "current position: $position")
                            }
                        )
                    }
                }
            }
        }
    }
}
