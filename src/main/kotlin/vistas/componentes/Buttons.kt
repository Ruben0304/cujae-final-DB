package vistas.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun SubmitButton() {
    var isSubmitted by remember { mutableStateOf(false) }

    val buttonColor by animateColorAsState(
        targetValue = if (isSubmitted) Color(0xFF4CAF50) else Color(0xFF0D47A1),
        animationSpec = tween(durationMillis = 600)
    )

    LaunchedEffect(isSubmitted) {
        if (isSubmitted) {
            delay(2000) // Esperar 2 segundos antes de volver al estado original
            isSubmitted = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(.5f)
            .padding(top = 30.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp)) // Aseguramos que toda la forma esté redondeada
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (isSubmitted) listOf(buttonColor, buttonColor)
                    else listOf(Color(0xFF0D47A1), Color(0xFF1976D2)) // Tonos azul oscuro
                )
            )
            .clickable {
                isSubmitted = true

            }
    ) {
        Box(
            contentAlignment = Alignment.Center, // Centrar el contenido dentro del botón
            modifier = Modifier.fillMaxSize()
        ) {
            if (isSubmitted) {
                Icon(
                    imageVector = Icons.Default.Check, // Ícono de éxito
                    contentDescription = "Success",
                    tint = Color.White
                )
            } else {
                Text("Crear perfil", color = Color.White)
            }
        }
    }
}