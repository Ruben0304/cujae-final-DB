package vistas.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import vistas.colores.textColor

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf(searchText) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused) Color(0xffffffff) else Color(0xf4ffffff),
        animationSpec = tween(durationMillis = 500)
    )

    TextField(
        value = query,
        onValueChange = { query = it }, // Actualiza el valor de `query`, pero no dispara la búsqueda
        placeholder = { Text("Buscar...", color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .onFocusChanged { focusState -> isFocused = focusState.isFocused },
        trailingIcon = {
            IconButton(
                onClick = {
                    onSearchTextChange(query) // Actualiza el texto de búsqueda
                    onSearchTriggered() // Realiza la búsqueda cuando se hace clic en el ícono
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = textColor
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            disabledTextColor = Color.Gray,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            cursorColor = textColor,
            errorCursorColor = Color.Red,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red
        ),
    )
}
