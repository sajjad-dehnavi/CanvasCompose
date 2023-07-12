package dehnavi.sajjad.canvascompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dehnavi.sajjad.canvascompose.ui.CustomCircularProgressIndicator
import dehnavi.sajjad.canvascompose.ui.theme.CanvasComposeTheme
import dehnavi.sajjad.canvascompose.ui.theme.darkGray
import dehnavi.sajjad.canvascompose.ui.theme.gray
import dehnavi.sajjad.canvascompose.ui.theme.orange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
