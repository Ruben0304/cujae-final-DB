//import androidx.compose.desktop.ui.tooling.preview.Preview
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.application
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Edit
//
//@Composable
//fun CRUDButtons(
//    onAdd: () -> Unit,
//    onUpdate: () -> Unit,
//    onDelete: () -> Unit
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceEvenly
//    ) {
//        Button(
//            onClick = onAdd,
//            colors = ButtonDefaults.buttonColors(containerColor = Color(89, 158, 94)),
//        ) {
//            Icon(Icons.Default.Add, contentDescription = "Agregar")
//            Spacer(Modifier.width(8.dp))
//            Text("Agregar", color = Color.White)
//        }
//
//        Button(
//            onClick = onUpdate,
//            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//        ) {
//            Icon(Icons.Default.Edit, contentDescription = "Actualizar")
//            Spacer(Modifier.width(8.dp))
//            Text("Actualizar")
//        }
//
//        Button(
//            onClick = onDelete,
//            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
//        ) {
//            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
//            Spacer(Modifier.width(8.dp))
//            Text("Eliminar")
//        }
//    }
//}
//
//@Composable
//fun GenericForm(
//    title: String,
//    fields: List<@Composable () -> Unit>,
//    onSubmit: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text(title, style = MaterialTheme.typography.headlineMedium) },
//        text = {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                fields.forEach { it() }
//            }
//        },
//        confirmButton = {
//            Button(onClick = onSubmit) {
//                Text("Enviar")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancelar")
//            }
//        }
//    )
//}
//
//@Composable
//@Preview
//fun MainScreen() {
//    var showForm by remember { mutableStateOf(false) }
//    var formType by remember { mutableStateOf("") }
//
//    MaterialTheme {
//        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//            CRUDButtons(
//                onAdd = {
//                    showForm = true
//                    formType = "Agregar"
//                },
//                onUpdate = {
//                    showForm = true
//                    formType = "Actualizar"
//                },
//                onDelete = {
//                    showForm = true
//                    formType = "Eliminar"
//                }
//            )
//
//            if (showForm) {
//                GenericForm(
//                    title = "$formType Entidad",
//                    fields = listOf(
//                        { OutlinedTextField(value = "", onValueChange = {}, label = { Text("Campo 1") }) },
//                        { OutlinedTextField(value = "", onValueChange = {}, label = { Text("Campo 2") }) },
//                        { OutlinedTextField(value = "", onValueChange = {}, label = { Text("Campo 3") }) }
//                    ),
//                    onSubmit = {
//                        println("Formulario enviado: $formType")
//                        showForm = false
//                    },
//                    onDismiss = { showForm = false }
//                )
//            }
//        }
//    }
//}
//
//fun main() = application {
//    Window(onCloseRequest = ::exitApplication) {
//        MainScreen()
//    }
//}