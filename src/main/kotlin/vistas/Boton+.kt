import androidx.compose.animation.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme(colorScheme = darkColorScheme()) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF121212) // Fondo oscuro
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FloatingActionButtonWithDialog(
                        onSubmit = {
                            // Acción al enviar el formulario
                            println("Formulario enviado")
                        },
                        onDismiss = {
                            // Acción al cancelar el formulario
                            println("Formulario cancelado")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingActionButtonWithDialog(
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    var state by remember { mutableStateOf(ButtonState.FAB) }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state == ButtonState.FAB,
            enter = fadeIn() + expandIn(expandFrom = Alignment.BottomEnd),
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.BottomEnd),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp) // Ajusta el espaciado para el FAB
        ) {
            FloatingActionButton(
                onClick = { state = ButtonState.DIALOG },
                containerColor = Color(16, 78, 146), // Color morado de Material Design
                contentColor = Color.White,
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer(
                        shadowElevation = 8f,
                        shape = CircleShape,
                        clip = true
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar",
                    tint = Color.White
                )
            }
        }

        AnimatedVisibility(
            visible = state == ButtonState.DIALOG,
            enter = fadeIn() + expandIn(),
            exit = fadeOut() + shrinkOut()
        ) {
            GenericForm(
                title = "Formulario",
                fields = listOf(
                    { TextField(value = "", onValueChange = {}, label = { Text("Campo 1") }) },
                    { TextField(value = "", onValueChange = {}, label = { Text("Campo 2") }) }
                ),
                onSubmit = {
                    onSubmit()
                    state = ButtonState.FAB // Regresa al estado FAB después de enviar
                },
                onDismiss = {
                    onDismiss()
                    state = ButtonState.FAB // Regresa al estado FAB después de cancelar
                }
            )
        }
    }
}

@Composable
fun GenericForm(
    title: String,
    fields: List<@Composable () -> Unit>,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fields.forEach { it() }
            }
        },
        confirmButton = {
            Button(onClick = onSubmit) {
                Text("Enviar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

enum class ButtonState {
    FAB,
    DIALOG
}
