package vistas

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import auth.Auth
import dao.*
import global.Global
import kotlinx.coroutines.launch
import modelos.Consulta
import modelos.Turno
import vistas.colores.textColor
import vistas.componentes.*
import vistas.nav.NavManager
import kotlin.math.roundToInt

@Composable
fun ConsultaTable(
    unidad: String? = null,
    departamento: String? = null,
    numeroTurno: Int? = null,
    medico: String? = null
) {
    val surfaceColor = Color(0xffffffff)
    val headerColor = Color(0xe4000000)
    val dividerColor = Color(0xba949494)
    val linkColor = Color(0xff5073ec)

    var consultas by remember { mutableStateOf(listOf<Consulta>()) }
    var isLoading by remember { mutableStateOf(true) }

    val corrutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        if (unidad != null && departamento != null && numeroTurno != null && medico != null)
            consultas = ConsultaDAO.getConsultasTurno(unidad, departamento, Auth.hospital, numeroTurno, medico)
        else
            consultas = ConsultaDAO.getConsultasMedico()
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {
            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(animationSpec = tween(durationMillis = 100))
            ) {
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(headerColor)
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            listOf("Consulta", "Paciente", "CI Paciente", "Fecha", "Estado").forEach { header ->
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = header,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                    )
                                }
                            }
                        }

                        // Rows
                        LazyColumn {
                            itemsIndexed(consultas) { index, consulta ->
                                SwipeableConsultaRow(
                                    consulta = consulta,
                                    onEdit = {
                                        EditDialogManager.showDialog(
                                            textoP = "Editar Consulta",
                                            acceptActionP = { updatedValues ->
                                                updatedValues.forEach(::println)
                                                corrutineScope.launch {
                                                    try {
                                                        DAOs.actualizarConsulta(
                                                            consultaId = consulta.consulta_id,
                                                            turnoNumero = consulta.turno_numero,
                                                            turnoUnidadCodigo = consulta.registro.unidad_codigo,
                                                            turnoDepartamentoCodigo = consulta.registro.departamento_codigo,
                                                            turnoHospitalCodigo = Auth.hospital,
                                                            fechaHora = updatedValues["fechaHora"] ?: ""
                                                        )
                                                    } catch (e: Exception) {
                                                        println(e.message)
                                                    }
                                                    NavManager.navController.currentDestination?.route?.let {
                                                        NavManager.navController.navigate(
                                                            it
                                                        )
                                                    }
                                                }
                                            },
                                            entidadP = "Consulta",
                                            initialValuesP = mapOf(
                                                "fechaHora" to consulta.fecha_hora
                                            )
                                        )
                                    },
                                    onDelete = {

                                        if (Auth.hospital != "")
                                            AceptCancelDialogManager.showDialog(
                                                "Seguro que deseas eliminar unidad ?",
                                                {
                                                    corrutineScope.launch {
                                                        ConsultaDAO.eliminar(
                                                            consulta.registro.unidad_codigo,
                                                            consulta.registro.departamento_codigo,
                                                            Auth.hospital,
                                                            consulta.id_medico,
                                                            consulta.turno_numero,
                                                            consulta.consulta_id,
                                                        )
                                                        NavManager.navController.navigate("consultas/${unidad}/${departamento}/${numeroTurno}/${medico}")
                                                    }
                                                })


                                    },
                                    index = index
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeableConsultaRow(
    consulta: Consulta,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    index: Int
) {
    val coroutineScope = rememberCoroutineScope()
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX)
    val animatedProgress = remember { androidx.compose.animation.core.Animatable(initialValue = 0f) }

    var expanded by remember { mutableStateOf(false) } // Controla si el menú está desplegado
    var selectedEstado by remember { mutableStateOf(consulta.registro.estado) } // Controla el estado seleccionado

    LaunchedEffect(key1 = Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 250,
                easing = EaseOutQuad,
                delayMillis = index * 60
            )
        )
    }

    val cambiarEstado = { estado: String ->
        println(estado)
        if (estado == "no atendido") {
            CausaDialogManager.showDialog(
                textoP = "Escriba la causa",
                acceptActionP = { causa ->
                    coroutineScope.launch {
                        try {
                            RegistroDAO.marcarNoAtendido(
                                consulta.registro.registro_id.toInt(),
                                consulta.registro.unidad_codigo,
                                consulta.registro.departamento_codigo,
                                Auth.hospital,
                                causa
                            )
                            // Actualiza el estado visual después de la operación
                            selectedEstado = "no_atendido"
                            ToastManager.showToast("Estado cambiado correctamente", ToastType.SUCCESS)
                        } catch (e: Exception) {
                            ToastManager.showToast("Error al cambiar el estado", ToastType.ERROR)
                        }
                        CausaDialogManager.hideDialog()
                    }
                }
            )
        } else {
            coroutineScope.launch {
                try {
                    RegistroDAO.cambiarEstadoEnRegistro(
                        p_departamento_codigo = consulta.registro.departamento_codigo,
                        p_hospital_codigo = Auth.hospital,
                        p_unidad_codigo = consulta.registro.unidad_codigo,
                        p_registro_id = consulta.registro.registro_id.toInt(),
                        p_nuevo_estado = estado
                    )
                    // Actualiza el estado visual después de la operación
                    selectedEstado = estado
                    ToastManager.showToast("Estado cambiado correctamente", ToastType.SUCCESS)
                } catch (e: Exception) {
                    ToastManager.showToast("Error al cambiar el estado", ToastType.ERROR)
                }
                println(consulta.registro.departamento_codigo)
                println(Auth.hospital)
                println(consulta.registro.unidad_codigo)
                println(consulta.registro.registro_id.toInt())
                println(estado)
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(animatedProgress.value)
            .offset(y = (20 * (1f - animatedProgress.value)).dp)
    ) {
        Row(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
                                offsetX = if (offsetX < -100f) -200f else 0f
                            }
                        }
                    ) { _, dragAmount ->
                        coroutineScope.launch {
                            offsetX = (offsetX + dragAmount).coerceIn(-200f, 0f)
                        }
                    }
                }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = consulta.consulta_id.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${consulta.registro.paciente.nombre} ${consulta.registro.paciente.apellidos}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = consulta.registro.paciente.ci,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = consulta.getFormattedFechaHora(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Estado con dropdown
            Box(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center)
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = { expanded = true }) // Abre menú con click
                    }
            ) {
                val icono = when (selectedEstado) {
                    "no_atendido" -> "❌"
                    "pendiente" -> "🕗"
                    "fallecido" -> "💀"
                    else -> "✅"
                }
                Text(icono)

                // DropdownMenu de selección de estado
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    EstadoDropdownItem("no atendido", "❌", selectedEstado) {
                        cambiarEstado(it)
                    }
                    EstadoDropdownItem("pendiente", "🕗", selectedEstado) {
                        cambiarEstado(it)
                    }
                    EstadoDropdownItem("fallecido", "💀", selectedEstado) {
                        cambiarEstado(it)
                    }
                    EstadoDropdownItem("atendido", "✅", selectedEstado) {
                        cambiarEstado(it)
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoDropdownItem(
    estado: String,
    emoji: String,
    selectedEstado: String,
    onSelected: (String) -> Unit
) {
    DropdownMenuItem(
        onClick = {
            onSelected(estado) // Ejecuta la acción de cambio de estado
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = selectedEstado == estado,
                onCheckedChange = {
                    onSelected(estado) // Actualiza la selección de estado
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("$emoji $estado")
        }
    }
}
