package vistas.componentes
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var formType by remember { mutableStateOf("personal") }

    val formFields = mapOf(
        "personal" to listOf(
            "Nombre" to "firstName",
            "Apellido" to "lastName",
            "Correo electrónico" to "email",
            "Teléfono" to "phone"
        ),
        "business" to listOf(
            "Nombre de la empresa" to "companyName",
            "Cargo" to "position",
            "Correo empresarial" to "businessEmail",
            "Teléfono de la empresa" to "businessPhone"
        )
    )

    MaterialTheme(colors = darkColors(
        primary = Color(0xFF1DA1F2),
        background = Color(0xFF15202B),
        surface = Color(0xFF192734),
        onBackground = Color.White,
        onSurface = Color.White
    )) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Formulario Dinámico",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground
                    )
                    CustomDropdown(
                        options = listOf("Personal", "Empresarial"),
                        selectedOption = if (formType == "personal") "Personal" else "Empresarial",
                        onOptionSelected = { formType = if (it == "Personal") "personal" else "business" }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                formFields[formType]?.chunked(2)?.forEach { rowFields ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowFields.forEach { (label, name) ->
                            CustomTextField(
                                label = label,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = { /* Handle form submission */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Enviar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CustomDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(73,80,93, 0xed))
        ) {
            Text(selectedOption, color = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colors.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(option, color = MaterialTheme.colors.onSurface)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(label: String, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)) },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            textColor = MaterialTheme.colors.onSurface,
            cursorColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.primary,
            unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Formulario Dinámico") {
        App()
    }
}