package vistas.componentes
import DialogButton
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowState
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import vistas.colores.doctorGradient

@Composable
fun CustomMaterial3Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .height(400.dp)
                .width(250.dp)
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Doctor",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun AnimatedDialogContent(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = tween(300)
        ),
        exit = fadeOut(animationSpec = tween(300)) + scaleOut(
            targetScale = 0.8f,
            animationSpec = tween(300)
        )
    ) {
        CustomMaterial3Dialog(onDismissRequest = onDismissRequest, content = content)
    }
}

@Composable
@Preview
fun MainContent() {
    var showDialog by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Abrir Diálogo")
            }

            AnimatedDialogContent(
                visible = showDialog,
                onDismissRequest = { showDialog = false }
            ) {
                RotatingCard(
                    frontGradient = doctorGradient,
                    labelText = "Doctor",
                    avatar = painterResource("a.jpg"),
                    titleText = "Akkjdkjdkjddkj",
                    subtitleText = "kdjdkjdkdjkd",
                    infoItems = listOf(
                        InfoItem(Icons.Rounded.Badge, "Número de Historia", "doctor.numeroLicencia"),
                        InfoItem(Icons.Rounded.Phone, "Teléfono", "doctor.telefono"),
                        InfoItem(Icons.Rounded.CalendarToday, "Años de exp.", "doctor"),
                        InfoItem(Icons.Rounded.Email, "Contacto", "doctor.datosContacto")
                    )
                ){
                    GlassmorphismDialogManager.showDialog(
                        listOf(
                            DialogButton(
                                "Aceptar",
                                "✅"
                            ) { println("Aceptar clicked"); GlassmorphismDialogManager.hideDialog() },
                            DialogButton(
                                "Cancelar",
                                "❌"
                            ) { println("Cancelar clicked"); GlassmorphismDialogManager.hideDialog() },
                            DialogButton("Más información", "ℹ️") { println("Más información clicked") }
                        )
                    )
                }
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Dialog Demo",
        state = WindowState(width = 600.dp, height = 600.dp)
    ) {
        MainContent()
    }
}

