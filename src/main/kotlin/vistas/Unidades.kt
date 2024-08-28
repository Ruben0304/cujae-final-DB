package vistas
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

@Composable
fun UnidadTable(
    unidades: List<Unidad>,
    onEditUnidad: (Unidad) -> Unit,
    onDeleteUnidad: (Unidad) -> Unit
) {
    val surfaceColor = Color(0xFF2D2D2D)
    val headerColor = Color(0xFF3700B3)
    val textColor = Color.White
    val dividerColor = Color(0xFF3D3D3D)
    val linkColor = Color(0xFF64B5F6)
    val editColor = Color(0xFF4CAF50)
    val deleteColor = Color(0xFFF44336)

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
                    listOf("Cod", "Nombre", "Departamento", "Ubicación", "", "").forEach { header ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = header,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
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
                                    onClick = { onEditUnidad(unidad) },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(8.dp),


                                ) {


                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color(43,98,218),
                                        modifier = Modifier.size(24.dp) // Bigger icon
                                    )

                                }
                                Spacer(modifier = Modifier.width(12.dp)) // Increased spacing for better aesthetics

                                IconButton(
                                    onClick = { onDeleteUnidad(unidad) },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(8.dp)
                                ) {

                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(150,42,55),
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
                                    Text(unidad.departamento, color = textColor)
                                }
                                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Text(unidad.ubi, color = textColor)
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp)) // Add rounded corners to the clickable area
                                        .background(Color.Transparent)
                                        .clickable { }
                                        .zIndex(3f), // Ensure this is above everything else
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Doctores",
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
                                        .clickable { }
                                        .zIndex(3f), // Ensure this is above everything else
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Pacientes",
                                        color = linkColor,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }

                        if (index < unidades.size - 1) {
                            Divider(color = dividerColor, thickness = 1.dp)
                        }
                    }
                }


            }
        }
    }
}
data class Unidad(
    val codigo: String,
    val nombre: String,
    val departamento: String,
    val ubi: String,

)


@Preview
@Composable
fun UnidadTablePreview() {
    val unidades = listOf(
        Unidad("04056", "Unidad A", "Cardiologia", "Pazillo 3"),
        Unidad("04056", "Unidad A", "Cardiologia", "Pazillo 3"),
        Unidad("04056", "Unidad A", "Cardiologia", "Pazillo 3"),
        Unidad("04056", "Unidad A", "Cardiologia", "Pazillo 3"),
        Unidad("04056", "Unidad A", "Cardiologia", "Pazillo 3"),
        Unidad("04056", "Unidad A", "Cardiologia", "Pazillo 3"),





        )
    UnidadTable(unidades,{},{})
}