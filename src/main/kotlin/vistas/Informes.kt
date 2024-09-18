package vistas

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import auth.Auth
import dao.ConsultaDAO
import dao.InformesDAO
import dao.TurnoDAO
import global.Global
import kotlinx.coroutines.launch
import modelos.Consulta
import modelos.Informe
import modelos.Turno
import vistas.colores.textColor

@Composable
fun InformesTable(unidad: String, departamento: String) {
    val surfaceColor = Color(0xffffffff)
    val headerColor = Color(0xe4000000)
    val dividerColor = Color(0xba949494)
    val linkColor = Color(0xff5073ec)

    var informes by remember { mutableStateOf(listOf<Informe>()) }
    var isLoading by remember { mutableStateOf(true) }


    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        if (Auth.hospital != "")
            informes = InformesDAO.obtenerInformes(unidad, departamento, Auth.hospital)
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
                                "#",
                                "Atendidos",
                                "Dados de alta",
                                "Admitidos",
                                "En registro",
                                "Fecha"
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
                            itemsIndexed(informes) { index, informe ->
                                val animatedProgress =
                                    remember { androidx.compose.animation.core.Animatable(initialValue = 0f) }

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
                                Column(
                                    modifier = Modifier
                                        .alpha(animatedProgress.value)
                                        .offset(y = (20 * (1f - animatedProgress.value)).dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 16.dp),

                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                informe.numeroInforme.toString(),
                                                color = textColor,
                                            )
                                        }
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                informe.pacientesAtendidos.toString(),
                                                color = textColor,
                                            )
                                        }
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                informe.pacientesAlta.toString(),
                                                color = textColor,
                                            )
                                        }
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                informe.pacientesAdmitidos.toString(),
                                                color = textColor,
                                            )
                                        }
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                informe.totalRegistro.toString(),
                                                color = textColor,
                                            )
                                        }
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                informe.getFormattedFechaHora(),
                                                color = textColor,
                                            )
                                        }


                                    }


                                }
//                                    if (index < consultas.size - 1) {
//                                        Divider(color = dividerColor, thickness = 1.dp)
//                                    }
                            }
                        }
                    }
                }
            }
        }
    }
}





