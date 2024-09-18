package vistas

import PatientListContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import auth.Auth
import dao.DAOs
import dao.DepartamentoDAO
import global.Global
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import modelos.Departamento
import vistas.colores.textColor
import vistas.componentes.EditDialogManager
import vistas.nav.NavManager
import kotlin.math.roundToInt


@Composable
fun DepartamentoTable(navController: NavHostController) {
    val surfaceColor = Color(0xffffffff)
    val headerColor = Color(0xe4000000)
    val dividerColor = Color(0xba949494)
    val linkColor = Color(0xff5073ec)
    var departamentos by remember { mutableStateOf(listOf<Departamento>()) }
    var isLoading by remember { mutableStateOf(true) }

    val corrutineScope = rememberCoroutineScope()


    LaunchedEffect(key1 = true) {

        if (Auth.hospital != "")
            departamentos = DepartamentoDAO.obtenerDepartamentosPorHospital(Auth.hospital)
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
                            listOf("Cod", "Nombre", "").forEach { header ->
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

                        LazyColumn {
                            itemsIndexed(departamentos) { index, departamento ->
                                val animatedProgress = remember {
                                    androidx.compose.animation.core.Animatable(
                                        initialValue = 0f
                                    )
                                }

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
                                        .alpha(animatedProgress.value)
                                        .offset(y = (20 * (1f - animatedProgress.value)).dp)
                                ) {
                                    SwipeableRow(
                                        departamento = departamento,
                                        onEdit = { EditDialogManager.showDialog(
                                            textoP = "Editar Departamento",
                                            acceptActionP = { updatedValues ->
                                                updatedValues.forEach(::println)
                                                corrutineScope.launch {
                                                    try {
                                                        DAOs.actualizarDepartamento(
                                                            codigo = departamento.departamento_codigo,
                                                            hospitalCodigo = Auth.hospital,
                                                            nombre = updatedValues["nombre"] ?: ""
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
                                            entidadP = "Departamento",
                                            initialValuesP = mapOf(
//                                                "hospitalCodigo" to Auth.hospital,
                                                "nombre" to departamento.departamento_nombre
                                            )
                                        ) },
                                        onDelete = {
                                            corrutineScope.launch {
                                                if (Auth.hospital != "")
                                                    DepartamentoDAO.eliminar(
                                                        departamento.departamento_codigo,
                                                        Auth.hospital
                                                    )
                                                NavManager.navController.navigate("departamentos")
                                            }

                                        },
                                        onViewUnits = {NavManager.navController.navigate("unidades/${departamento.departamento_codigo}") }
                                    )
                                }

                                if (index < departamentos.size - 1) {
                                    Divider(color = dividerColor, thickness = 0.5.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeableRow(
    departamento: Departamento,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewUnits: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX)

    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                text = departamento.departamento_codigo,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = departamento.departamento_nombre,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = onViewUnits
                ) {
                    Text("Ver unidades")
                }
            }
        }
    }
}