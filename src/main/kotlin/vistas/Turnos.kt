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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import auth.Auth
import dao.PatientDAO
import dao.TurnoDAO
import dao.UnidadDAO
import global.Global
import kotlinx.coroutines.launch
import modelos.Departamento
import modelos.Patient
import modelos.Turno
import vistas.colores.textColor
import vistas.componentes.AceptCancelDialogManager
import vistas.componentes.ToastHost
import vistas.componentes.ToastManager
import vistas.componentes.ToastType
import vistas.nav.NavManager
import kotlin.math.roundToInt

@Composable
fun TurnoTable(unidad: String? = null, departamento: String? = null) {
    val surfaceColor = Color(0xffffffff)
    val headerColor = Color(0xe4000000)
    val dividerColor = Color(0xba949494)
    val linkColor = Color(0xff5073ec)
    var turnos by remember { mutableStateOf(listOf<Turno>()) }
    var isLoading by remember { mutableStateOf(true) }

    val corrutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {

        try {
            if (unidad != null && departamento != null)
                turnos = TurnoDAO.getTurnosUnidad(unidad, departamento, Auth.hospital)
            else
                turnos = TurnoDAO.getTurnosMedico()
        } catch (e: Exception) {
            println(e.message)
        }

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
                            listOf(
                                "Turno",
                                if (Auth.rol != "medico") "Doctor" else "",
                                "Total",
                                "Atendidos",
                                "Estado",
                                ""
                            ).forEach { header ->
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
                            itemsIndexed(turnos) { index, turno ->
                                SwipeableTurnoRow(
                                    turno = turno,
                                    onEdit = { ToastManager.showToast("Los turnos no son editables",ToastType.INFO) },
                                    onDelete = {

                                        corrutineScope.launch {
                                            if (unidad != null) {
                                                if (departamento != null) {
                                                    TurnoDAO.eliminar(
                                                        turno.numeroTurno,
                                                        unidad,
                                                        departamento,
                                                        Auth.hospital,
                                                        turno.medicoCodigo
                                                    )
                                                }
                                            }
                                            NavManager.navController.navigate("turnos/${unidad}/${departamento}")
                                        }


                                    },
                                    onViewConsultas = { NavManager.navController.navigate("consultas/${unidad}/${departamento}/${turno.numeroTurno}/${turno.medicoCodigo}") },
                                    linkColor = linkColor,
                                    index = index
                                )
//                                if (index < turnos.size - 1) {
//                                    Divider(color = dividerColor, thickness = 1.dp)
//                                }
                            }
                        }
                    }
                }
            }
        }
    }
    ToastHost()
}

@Composable
fun SwipeableTurnoRow(
    turno: Turno,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewConsultas: () -> Unit,
    linkColor: Color,
    index: Int
) {
    val coroutineScope = rememberCoroutineScope()
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX)
    val animatedProgress = remember { androidx.compose.animation.core.Animatable(initialValue = 0f) }

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(animatedProgress.value)
            .offset(y = (20 * (1f - animatedProgress.value)).dp)
    ) {
        // Icons (Edit and Delete) - To the right, initially hidden
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .alpha((-offsetX / 200f).coerceIn(0f, 1f))
                .padding(end = 16.dp)
        ) {
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color(43, 98, 218),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(150, 42, 55),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Row content
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
                text = turno.numeroTurno.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            if(Auth.rol != "medico")
            Text(
                text =  "${turno.medicoNombre} ${turno.medicoApellidos}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = turno.totalPacientes.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = turno.pacientesAtendidos.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.weight(1f)) {
                val progressAnimatedValue = remember { androidx.compose.animation.core.Animatable(0f) }
                LaunchedEffect(turno) {
                    if (turno.totalPacientes > 0) {
                        progressAnimatedValue.animateTo(
                            targetValue = turno.pacientesAtendidos.toFloat() / turno.totalPacientes.toFloat(),
                            animationSpec = tween(1000, easing = FastOutSlowInEasing)
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = 1f,
                    color = Color(0xFF424242),
                    modifier = Modifier
                        .height(6.dp)
                        .fillMaxWidth()
                )
                LinearProgressIndicator(
                    progress = progressAnimatedValue.value,
                    color = if (turno.esExitoso()) Color(0xFF00C853) else Color(0xFFFF5252),
                    modifier = Modifier
                        .height(6.dp)
                        .fillMaxWidth()
                )
            }
            if (Auth.rol != "medico")
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ver",
                        color = linkColor,
                        modifier = Modifier
                            .clickable(onClick = onViewConsultas)
                            .padding(4.dp)
                    )
                }
        }
    }
}


//@Preview
//@Composable
//fun TurnosTablePreview() {
//    val turnos = listOf(
//        Turno("1", 60, 40),
//        Turno("2", 30, 28),
//        Turno("3", 20, 8),
//        Turno("1", 60, 40),
//
//        )
//    TurnoTable(turnos)
//}