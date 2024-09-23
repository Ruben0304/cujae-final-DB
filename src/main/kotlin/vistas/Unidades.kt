package vistas

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import auth.Auth
import dao.DAOs
import dao.DepartamentoDAO
import dao.UnidadDAO
import global.Global
import kotlinx.coroutines.launch
import modelos.Departamento
import modelos.Unidad
import vistas.colores.textColor
import vistas.componentes.AceptCancelDialogManager
import vistas.componentes.EditDialogManager
import vistas.componentes.ToastManager
import vistas.componentes.ToastType
import vistas.nav.NavManager


@Composable
fun UnidadTable(
    departamentoCodigo: String = "",
    onNavigateToPacientes: (String, String) -> Unit,
    onNavigateToTurnos: (String, String) -> Unit,
    onNavigateToMedicos: (String, String) -> Unit,
    onNavigateToInformes: (String, String) -> Unit
) {
    val surfaceColor = Color(0xffffffff)
    val headerColor = Color(0xe4000000)
    val dividerColor = Color(0xba949494)
    val linkColor = Color(0xff5073ec)
    val editColor = Color(0xFF4CAF50)
    val deleteColor = Color(0xFFF44336)

    var unidades by remember { mutableStateOf(listOf<Unidad>()) }
    var isLoading by remember { mutableStateOf(true) }

    val corrutineScope = rememberCoroutineScope()


    LaunchedEffect(key1 = true) {
        if (Auth.hospital != "")
            unidades = if (departamentoCodigo == "")
                UnidadDAO.obtenerUnidadesHospital(Auth.hospital)
            else
                UnidadDAO.obtenerUnidadesPorHospitalYDepartamento(Auth.hospital, departamentoCodigo)
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
                androidx.compose.material3.CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {
            Surface(
                color = surfaceColor,
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp,
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
                        listOf("Cod", "Nombre", "Ubicación", "", "", "", "").forEach { header ->
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
                        itemsIndexed(unidades) { index, unidad ->
                            var offsetX by remember { mutableStateOf(0f) }
                            val animatedOffsetX by animateFloatAsState(targetValue = offsetX)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                            ) {
                                // Options (Edit and Delete) - Behind the row content
                                Row(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 16.dp)
                                        .zIndex(1f) // To ensure it doesn't go under other elements
                                ) {

                                    IconButton(
                                        onClick = { EditDialogManager.showDialog(
                                            textoP = "Editar unidad",
                                            acceptActionP = { updatedValues ->
                                                updatedValues.forEach(::println)
                                                corrutineScope.launch {
                                                    try {
                                                        DAOs.actualizarUnidad(
                                                            codigo = unidad.codigo,
                                                            hospitalCodigo = Auth.hospital,
                                                            ubicacion = updatedValues["ubicacion"] ?: "",
                                                            nombre = updatedValues["nombre"] ?: "",
                                                            departamentoCodigo = unidad.departamento
                                                        )
                                                    } catch (e: Exception) {
                                                        println(e.message)
                                                    }
                                                    NavManager.refresh()
                                                }
                                            },
                                            entidadP = "Unidad",
                                            initialValuesP = mapOf(
//                                                "hospitalCodigo" to Auth.hospital,
                                                "nombre" to unidad.nombre,
                                                "ubicacion" to unidad.ubicacion
                                            )
                                        )  },
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(8.dp),


                                        ) {


                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color(43, 98, 218),
                                            modifier = Modifier.size(24.dp) // Bigger icon
                                        )

                                    }
                                    Spacer(modifier = Modifier.width(12.dp)) // Increased spacing for better aesthetics

                                    IconButton(
                                        onClick = {
                                            corrutineScope.launch {
                                                UnidadDAO.eliminar(
                                                    unidad.codigo,
                                                    unidad.departamento,
                                                    Auth.hospital
                                                )
                                                NavManager.navController.navigate("unidades/${unidad.departamento}")
                                                ToastManager.showToast("Eliminado correctamente",ToastType.SUCCESS)
                                            }


                                        },
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(8.dp)
                                    ) {

                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(150, 42, 55),
                                            modifier = Modifier.size(24.dp) // Bigger icon
                                        )
                                    }
                                }

                                // Row content - In front of the options row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset { IntOffset(animatedOffsetX.dp.roundToPx(), 0) }
                                        .draggable(
                                            orientation = Orientation.Horizontal,
                                            state = rememberDraggableState { delta ->
                                                offsetX = (offsetX + delta).coerceIn(-200f, 0f)
                                            },
                                            onDragStopped = {
                                                if (offsetX < -100f) {
                                                    // Show options
                                                    offsetX = -200f
                                                } else {
                                                    // Reset position
                                                    offsetX = 0f
                                                }
                                            }
                                        )
                                        .background(surfaceColor)
                                        .padding(vertical = 12.dp, horizontal = 16.dp)
                                        .zIndex(2f), // Ensure the row content is above the buttons
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                        Text(unidad.codigo, color = textColor)
                                    }
                                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                        Text(unidad.nombre, color = textColor)
                                    }
                                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                        Text(unidad.ubicacion, color = textColor)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(8.dp)) // Add rounded corners to the clickable area
                                            .background(Color.Transparent)
                                            .clickable { onNavigateToTurnos(unidad.codigo, unidad.departamento) }
                                            .zIndex(3f), // Ensure this is above everything else
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Turnos",
                                            color = linkColor,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                    // Ver link
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Médicos",
                                            color = linkColor,
                                            modifier = Modifier
                                                .clickable {
                                                    onNavigateToMedicos(unidad.departamento, unidad.codigo)
                                                }
                                                .padding(4.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Transparent)
                                            .clickable { onNavigateToPacientes(unidad.codigo, unidad.departamento) }
                                            .zIndex(3f), // Ensure this is above everything else
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Pacientes",
                                            color = linkColor,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp)
                                            .clip(RoundedCornerShape(8.dp))

                                            .background(Color.Transparent)
                                            .clickable { onNavigateToInformes(unidad.codigo, unidad.departamento) }
                                            .zIndex(3f), // Ensure this is above everything else
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Informes",
                                            color = linkColor,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }

//                            if (index < unidades.size - 1) {
//                                Divider(color = dividerColor, thickness = 1.dp)
//                            }
                        }
                    }
                }

            }
        }
    }
}

