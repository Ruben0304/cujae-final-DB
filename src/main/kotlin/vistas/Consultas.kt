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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
fun ConsultaTable(consultas: List<Consulta>) {
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
                    listOf("Consulta", "Paciente", "Doctor", "Estado").forEach { header ->
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
                    itemsIndexed(consultas) { index, consulta ->
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(consulta.numero, color = textColor, modifier = Modifier.weight(1f))
                                Text(consulta.paciente, color = textColor, modifier = Modifier.weight(1f))
                                Text(consulta.doctor, color = textColor, modifier = Modifier.weight(1f))

                                Box(modifier = Modifier.weight(1f)) {
                                    if (consulta.estado)
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = Color.Green,
                                        )
                                    else
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            tint = Color.Red,
                                        )

                                }


                            }
                            if (index < consultas.size - 1) {
                                Divider(color = dividerColor, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Consulta(val numero: String, val paciente: String, val doctor: String, val estado: Boolean = true)


@Preview
@Composable
fun ConsultasTablePreview() {
    val consultas = listOf(
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez", false),
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez", false),
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez", false),

        )
    ConsultaTable(consultas)
}