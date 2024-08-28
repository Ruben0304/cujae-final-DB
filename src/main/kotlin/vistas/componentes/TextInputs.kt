package vistas.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            color = Color(0xFFAAAAAA),
            fontSize = 12.sp // Reducir el tamaño del texto del label
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFFFFFFFF),
                unfocusedTextColor = Color(0xFF888888),
                focusedContainerColor = Color(0xFF2A2A2A),
                unfocusedContainerColor = Color(0xFF2A2A2A),
                cursorColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color(0xFF7C4DFF),
                unfocusedIndicatorColor = Color(0xFF888888)
            ),
            textStyle = TextStyle(fontSize = 14.sp), // Reducir el tamaño del texto del campo
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(vertical = 4.dp) // Reducir el padding vertical
        )
    }
}

@Composable
fun TextAreaField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Column {
        Text(
            text = label,
            color = Color(0xFFAAAAAA),
            fontSize = 12.sp // Reducir el tamaño del texto del label
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Reducir la altura del área de texto
                .padding(vertical = 4.dp), // Reducir el padding vertical
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFFFFFFFF),
                unfocusedTextColor = Color(0xFF888888),
                focusedContainerColor = Color(0xFF2A2A2A),
                unfocusedContainerColor = Color(0xFF2A2A2A),
                cursorColor = Color(0xFFFFFFFF),
                focusedIndicatorColor = Color(0xFF7C4DFF),
                unfocusedIndicatorColor = Color(0xFF888888)
            ),
            textStyle = TextStyle(fontSize = 14.sp), // Reducir el tamaño del texto del campo
            maxLines = 4 // Reducir el número máximo de líneas
        )
    }
}