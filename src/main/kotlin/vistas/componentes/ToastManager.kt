package vistas.componentes

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import vistas.login.StartLoading

enum class ToastType {
    SUCCESS, ERROR, WARNING, INFO
}

data class ToastData(val message: String, val type: ToastType)

object ToastManager {
    private val _toastState = mutableStateOf<ToastData?>(null)
    val toastState: State<ToastData?> = _toastState

    fun showToast(message: String, type: ToastType) {
        _toastState.value = ToastData(message, type)
    }

    fun hideToast() {
        _toastState.value = null
    }
}

@Composable
fun ToastHost() {
    val toastData by ToastManager.toastState

    LaunchedEffect(toastData) {
        if (toastData != null) {
            delay(3000)
            ToastManager.hideToast()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = toastData != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            toastData?.let { ShowToast(it.message, it.type) }
        }
    }
}

@Composable
fun ShowToast(message: String, type: ToastType) {
    val (gradientColors, icon) = when (type) {
        ToastType.SUCCESS -> Pair(listOf(Color(0xff034920), Color(0xff1a5b3b)), Icons.Default.Check)
        ToastType.ERROR -> Pair(listOf(Color(0xFFFF3D00), Color(0xFFFF8A65)), Icons.Default.Error)
        ToastType.WARNING -> Pair(listOf(Color(0xFFFFA000), Color(0xFFFFCC80)), Icons.Default.Warning)
        ToastType.INFO -> Pair(listOf(Color(0xFF2196F3), Color(0xFF64B5F6)), Icons.Default.Info)
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(gradientColors))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Ejemplos de uso
@Composable
fun ToastExamples() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { ToastManager.showToast("¡Operación exitosa!", ToastType.SUCCESS) }) {
            Text("Mostrar Toast de Éxito")
        }
        Button(onClick = { ToastManager.showToast("Ha ocurrido un error", ToastType.ERROR) }) {
            Text("Mostrar Toast de Error")
        }
        Button(onClick = { ToastManager.showToast("Advertencia: batería baja", ToastType.WARNING) }) {
            Text("Mostrar Toast de Advertencia")
        }
        Button(onClick = { ToastManager.showToast("Nueva actualización disponible", ToastType.INFO) }) {
            Text("Mostrar Toast de Información")
        }
    }
    ToastHost()
}

// Ejemplo de uso en una función que podría lanzar una excepción
fun realizarOperacion() {
    try {
        // Simulación de una operación que puede fallar
        if (Math.random() < 0.5) throw Exception("Error aleatorio")
        ToastManager.showToast("Operación realizada con éxito", ToastType.SUCCESS)
    } catch (e: Exception) {
        ToastManager.showToast("Error: ${e.message}", ToastType.ERROR)
    }
}

// Ejemplo de uso en un ViewModel
class MiViewModel {
    fun cargarDatos() {
        // Simulación de carga de datos
        ToastManager.showToast("Datos cargados correctamente", ToastType.SUCCESS)
    }

    fun guardarCambios() {
        // Simulación de guardado de cambios
        ToastManager.showToast("Cambios guardados", ToastType.SUCCESS)
    }
}

// Ejemplo de uso en una pantalla principal
@Composable
fun MainScreen(viewModel: MiViewModel) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = { viewModel.cargarDatos() }) {
            Text("Cargar Datos")
        }
        Button(onClick = { viewModel.guardarCambios() }) {
            Text("Guardar Cambios")
        }
        Button(onClick = { realizarOperacion() }) {
            Text("Realizar Operación")
        }
    }
    ToastHost()
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MainScreen(MiViewModel())
    }
}