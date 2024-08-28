package vistas.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SelectInputFieldFiltrado(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    enabled: Boolean
) {
    Column {
        Text(
            text = label,
            color = Color(0xFFAAAAAA),
            fontSize = 12.sp
        )
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
                .clickable(enabled = enabled) { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.ifEmpty { "Selecciona una opción" },
                    color = if (value.isEmpty()) Color(0xFF888888) else Color(0xFFFFFFFF),
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = if (enabled) Color.White else Color(0xFF888888)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color(0xFF2A2A2A))
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        enabled = enabled
                    )
                }
            }
        }
    }
}



@Composable
fun SelectInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>
) {
    Column {
        Text(
            text = label,
            color = Color(0xFFAAAAAA),
            fontSize = 12.sp // Reducir el tamaño del texto del label
        )
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp) // Reducir el padding vertical
                .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = value.ifEmpty { "Selecciona una opción" },
                color = if (value.isEmpty()) Color(0xFF888888) else Color(0xFFFFFFFF),
                modifier = Modifier
                    .padding(12.dp) // Reducir el padding interno
                    .fillMaxWidth(),
                fontSize = 14.sp // Reducir el tamaño del texto
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }, // Reducir el tamaño del texto de las opciones
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}