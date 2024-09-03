package vistas.componentes

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegistrationFormDialog() {
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var additionalField by remember { mutableStateOf("") }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        border = ButtonDefaults.outlinedBorder
    ) {
        Text("Abrir formulario")
    }

    AnimatedVisibility(
        visible = showDialog,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                modifier = Modifier.width(400.dp).height(500.dp),
                color = Color(0xFF111827),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Formulario de registro",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        IconButton(onClick = { showDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    }
                    Text(
                        "Complete el formulario para registrarse. Haga clic en enviar cuando termine.",
                        color = Color(0xFFa0aec0),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomOutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nombre",
                            placeholder = "Ingrese su nombre"
                        )
                        CustomOutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Correo electrónico",
                            placeholder = "Ingrese su correo"
                        )
                        CustomOutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Contraseña",
                            placeholder = "Cree una contraseña"
                        )
                        CustomOutlinedTextField(
                            value = additionalField,
                            onValueChange = { additionalField = it },
                            label = "Campo adicional",
                            placeholder = "Información extra"
                        )
                        // Puedes añadir más campos aquí si es necesario
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Aquí puedes manejar la lógica de envío del formulario
                            println("Formulario enviado")
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2563eb))
                    ) {
                        Text("Enviar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White, fontSize = 12.sp) },
        placeholder = { Text(placeholder, fontSize = 12.sp) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White,
            backgroundColor = Color(0xFF1f2937),
            focusedBorderColor = Color(0xFF4b5563),
            unfocusedBorderColor = Color(0xFF4b5563)
        ),
        modifier = Modifier.fillMaxWidth().height(52.dp)
    )
}

@Preview
@Composable
fun PreviewRegistrationFormDialog() {
    MaterialTheme {
        Surface {
            RegistrationFormDialog()
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        PreviewRegistrationFormDialog()
    }
}