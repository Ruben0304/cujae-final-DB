package vistas.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun StartLoading(){
    var showProgress by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(true) }
// Retraso para mostrar el CircularProgressIndicator
    LaunchedEffect(Unit) {
        delay(1000) // 1 segundo de retraso
        showProgress = true
    }

    LaunchedEffect(Unit) {
        // Simular carga de datos desde la API
        delay(3000)
        isLoading = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212) // Fondo oscuro
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                // Mostrar circular progress bar
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource("Hospital Sign.png"),
                            contentDescription = "logo",
                            modifier = Modifier
                                .size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(35.dp))
                        AnimatedVisibility(
                            visible = showProgress,
                            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) // Fade in suave
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(55.dp),
                                color = Color.Red
                            )
                        }
                    }
                }

            } else {
                        LoginScreen()
            }
        }
    }
}
