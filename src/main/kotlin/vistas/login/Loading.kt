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
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import supabase.Supabase
import vistas.componentes.ToastHost
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

import java.net.InetSocketAddress
import java.net.Socket

@Composable
fun StartLoading() {
    var showProgress by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var internet by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        delay(1000) // 1 second delay
        showProgress = true
//        delay(1000)
       Supabase.coneccion.auth.admin.retrieveUsers()
//        auth.Auth.cambiar_rol("d1bcbe10-04bb-4386-a6b3-5d8b5b440f6f","service_role")
        isLoading = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212) // Dark background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                // Loading UI
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
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(35.dp))
                        AnimatedVisibility(
                            visible = showProgress,
                            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(45.dp),
                                color = Color.Red
                            )
                        }
                    }
                }
            } else {
//                LoginScreen()
            }

            // Show toast when there's no internet connection
            if (showToast) {
                ToastManager.showToast("Error de conexión a internet",ToastType.ERROR)
//                ShowToast("Error de conexión a internet", false)
                // Reset showToast after displaying
                LaunchedEffect(showToast) {
                    delay(3000) // Show toast for 3 seconds
                    showToast = false
                }
            }
        }
    }
    ToastHost()
}

suspend fun isInternetAvailable(): Boolean = withContext(Dispatchers.IO) {
    try {
        val socket = Socket()
        val socketAddress = InetSocketAddress("8.8.8.8", 53)
        socket.connect(socketAddress, 1500)
        socket.close()
        true
    } catch (e: Exception) {
        false
    }
}