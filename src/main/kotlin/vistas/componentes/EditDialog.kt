package vistas.componentes

import EditConsultaForm
import EditDepartamentoForm
import EditHospitalForm
import EditMedicoForm
import EditPacienteForm
import EditUnidadForm
import ElegantButton
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



object EditDialogManager {
    private val isDialogOpenState = mutableStateOf(false)
    private val texto = mutableStateOf("")
    private val entidad = mutableStateOf("")
    private var acceptAction : ((Map<String, String>) -> Unit)? = null
    private var initialValues = mutableStateOf<Map<String, String>>(mapOf())

    fun showDialog(
        textoP: String,
        acceptActionP: (Map<String, String>) -> Unit,
        entidadP: String,
        initialValuesP: Map<String, String>
    ) {
        isDialogOpenState.value = true
        texto.value = textoP
        entidad.value = entidadP
        acceptAction = acceptActionP
        initialValues.value = initialValuesP
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
        val windowState = rememberDialogState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(400.dp, 500.dp)
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
                    EditDialog(
                        onDismiss = { isLoading = true; hideDialog() },
                        onAccept = { updatedValues ->
                            acceptAction?.invoke(updatedValues)
                            hideDialog()
                        },
                        texto = texto.value,
                        entidad = entidad.value,
                        initialValues = initialValues.value
                    )
                }
        }
    }
}

@Composable
fun EditDialog(
    onDismiss: () -> Unit,
    onAccept: (Map<String, String>) -> Unit,
    texto: String,
    entidad: String,
    initialValues: Map<String, String>
) {
    var formValues by remember { mutableStateOf(mapOf<String, String>()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
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

                // Muestra el formulario correspondiente
                formValues = when (entidad) {
                    "Consulta" -> EditConsultaForm(initialValues)
                    "Departamento" -> EditDepartamentoForm(initialValues)
                    "Hospital" -> EditHospitalForm(initialValues)
                    "Medico" -> EditMedicoForm(initialValues)
                    "Paciente" -> EditPacienteForm(initialValues)
//                    "Turno" -> EditTurnoForm(initialValues)
                    "Unidad" -> EditUnidadForm(initialValues)
                    else -> {
                        ToastManager.showToast("Error: Entidad no reconocida", ToastType.ERROR)
                        mapOf()
                    }
                }

//                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ElegantButton(
                        text = "Aceptar",
                        emoji = "✅",
                        onClick = {onAccept(formValues)}
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


