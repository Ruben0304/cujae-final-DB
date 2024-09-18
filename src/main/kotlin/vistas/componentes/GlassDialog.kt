import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vistas.componentes.MacOSTitleBar
import kotlin.math.truncate

object GlassmorphismDialogManager {
    private val isDialogOpenState = mutableStateOf(false)
    private val dialogButtonsState = mutableStateOf<List<DialogButton>>(emptyList())

    fun showDialog(buttons: List<DialogButton>) {
        dialogButtonsState.value = buttons
        isDialogOpenState.value = true
    }
    val isDialogOpen: Boolean
        get() = isDialogOpenState.value

    fun hideDialog() {
        isDialogOpenState.value = false
    }

    @Composable
    fun DialogHost() {
        var isLoading by remember { mutableStateOf(true) }
        val isDialogOpen by isDialogOpenState
        val dialogButtons by dialogButtonsState
        val windowState = rememberDialogState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(780.dp, 230.dp)
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
                onKeyEvent =  { keyEvent ->
                // Detectar la tecla Esc
                if (keyEvent.key.nativeKeyCode == Key.Escape.nativeKeyCode) {
                   hideDialog()
                    true // Evento manejado
                } else {
                    false // Evento no manejado
                }
            }
            ) {
                GlassmorphismDialog(
                    onDismiss = { isLoading = true;hideDialog(); },
                    buttons = dialogButtons
                )
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GlassmorphismDialog(
    onDismiss: () -> Unit,
    buttons: List<DialogButton> // Asegúrate de que ButtonData es un tipo definido que contenga el texto y la acción
) {
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Contenido del diálogo
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(animatedScale)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xb624272c),
                            Color(0xad121b2f)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .border(.8.dp, Color(125, 138, 150, 0xdf), RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {

                // Título y contenido del diálogo
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "🤔 ¿Qué acciones desea realizar?",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de acción
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        buttons.forEach { button ->
                            ElegantButton(
                                text = button.text,
                                emoji = button.emoji,
                                onClick = button.onClick
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ElegantButton(
    text: String,
    emoji: String,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.05f else 1f,
        animationSpec = tween(300)
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(53,63,82,221)),
        modifier = Modifier
            .padding(8.dp)
            .scale(scale)
            .pointerMoveFilter(
                onEnter = { isHovered = true; false },
                onExit = { isHovered = false; false }
            ),

    ) {
        Text(
            "$emoji $text",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

data class DialogButton(
    val text: String,
    val emoji: String,
    val onClick: () -> Unit
)