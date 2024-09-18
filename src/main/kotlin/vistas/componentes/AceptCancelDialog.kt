package vistas.componentes

import DialogButton
import ElegantButton
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object AceptCancelDialogManager {
    private val isDialogOpenState = mutableStateOf(false)
    private val texto = mutableStateOf("")
    private var acceptAction: ((String) -> Unit)? = null // Se ajusta para aceptar el texto

    //publicas
    val haveTextField = mutableStateOf(false)
    val textfieldValor = mutableStateOf("")

    fun showDialog(textoP: String, acceptActionP: (String) -> Unit, haveText: Boolean = false) {
        haveTextField.value = haveText
        isDialogOpenState.value = true
        texto.value = textoP
        acceptAction = acceptActionP // Guardamos la acción que acepta el texto
    }

    val isDialogOpen: Boolean
        get() = isDialogOpenState.value

    fun hideDialog() {
        isDialogOpenState.value = false
        textfieldValor.value = "" // Restablecemos el valor del TextField
    }

    @Composable
    fun DialogHost() {
        var isLoading by remember { mutableStateOf(true) }
        val isDialogOpen by isDialogOpenState
        val windowState = rememberDialogState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(400.dp, 230.dp)
        )
        if (isDialogOpen) {
            rememberCoroutineScope().launch {
                delay(300)
                isLoading = false
            }
            if (!isLoading)
                DialogWindow(
                    onCloseRequest = { hideDialog() },
                    state = windowState,
                    undecorated = true,
                    transparent = true,
                    onKeyEvent = { keyEvent ->
                        if (keyEvent.key.nativeKeyCode == Key.Escape.nativeKeyCode) {
                            hideDialog()
                            true
                        } else {
                            false
                        }
                    }
                ) {
                    AceptCancelDialog(
                        onDismiss = { isLoading = true; hideDialog() },
                        onAccept = {
                            acceptAction
                            hideDialog()
                        },
                        texto = texto.value
                    )
                }
        }
    }
}

@Composable
fun AceptCancelDialog(
    onDismiss: () -> Unit,
    onAccept: (String) -> Unit, // Acción de aceptar con el texto del TextField
    texto: String
) {
    var textovalor by remember { mutableStateOf(TextFieldValue("")) }
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(500)
    )

    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    // Fondo desenfocado
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(animatedScale)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xb624272c), Color(0xad121b2f))
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .border(.8.dp, Color(125, 138, 150, 0xdf), RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    texto,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (AceptCancelDialogManager.haveTextField.value)
                    OutlinedTextField(
                        value = textovalor,
                        onValueChange = { textovalor = it },
                        placeholder = { Text("Escribe aquí", color = Color.Gray) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .border(1.dp, Color(0xFF607D8B), RoundedCornerShape(8.dp))
                    )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ElegantButton(
                        text = "Aceptar",
                        emoji = "✅",
                        onClick = { onAccept(textovalor.text) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ElegantButton(
                        text = "Cancelar",
                        emoji = "❌",
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}



