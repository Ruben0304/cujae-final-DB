package vistas

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun TurnoTable(turnos: List<Turno>) {
    val surfaceColor = Color(0xFF2D2D2D)
    val headerColor = Color(0xFF3700B3)
    val textColor = Color.White
    val dividerColor = Color(0xFF3D3D3D)
    val linkColor = Color(0xFF64B5F6)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
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
                    listOf("Turno", "Doctor", "Total", "Atendidos", "Estado", "").forEach { header ->
                        Text(
                            text = header,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Rows
                LazyColumn {
                    itemsIndexed(turnos) { index, turno ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) { Text(turno.numero, color = textColor) }
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) { Text(turno.doctor, color = textColor) }
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) { Text(turno.totalPacientes.toString(), color = textColor) }
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) { Text(turno.pacientesAtendidos.toString(), color = textColor) }


                                Box(modifier = Modifier.weight(1f)) {
                                    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }
                                    LaunchedEffect(turno) {
                                        animatedProgress.animateTo(
                                            targetValue = turno.pacientesAtendidos / turno.totalPacientes.toFloat(),
                                            animationSpec = tween(1000, easing = FastOutSlowInEasing)
                                        )
                                    }

                                    // Background progress bar
                                    LinearProgressIndicator(
                                        progress = 1f,
                                        color = Color(0xFF424242),
                                        modifier = Modifier
                                            .height(6.dp)
                                            .fillMaxWidth()
                                    )
                                    // Animated foreground progress bar
                                    LinearProgressIndicator(
                                        progress = animatedProgress.value,
                                        color = if (turno.esExitoso()) Color(0xFF00C853) else Color(0xFFFF5252),
                                        modifier = Modifier
                                            .height(6.dp)
                                            .fillMaxWidth()
                                    )
                                }

                                // Ver link
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Ver",
                                        color = linkColor,
                                        modifier = Modifier
                                            .clickable { }
                                            .padding(4.dp)
                                    )
                                }
                            }
                            if (index < turnos.size - 1) {
                                Divider(color = dividerColor, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Turno(
    val numero: String,
    val totalPacientes: Int,
    val pacientesAtendidos: Int,
    val doctor: String = "Gerardo "
) {
    fun esExitoso(): Boolean {
        return (pacientesAtendidos / totalPacientes.toFloat()) >= 0.8f
    }
}


@Preview
@Composable
fun TurnosTablePreview() {
    val turnos = listOf(
        Turno("1", 60, 40),
        Turno("2", 30, 28),
        Turno("3", 20, 8),
        Turno("1", 60, 40),

        )
    TurnoTable(turnos)
}