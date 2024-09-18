//package vistas.form.edit
//
//
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import dao.PatientDAO
//import kotlinx.coroutines.launch
//import vistas.componentes.SubmitButton
//import vistas.componentes.TextAreaField
//import vistas.componentes.TextInputField
//import java.time.LocalDate
//
//@Composable
//fun EditPacienteForm() {
//    var numeroHistoriaClinica by remember { mutableStateOf("") }
//    var nombre by remember { mutableStateOf("") }
//    var apellidos by remember { mutableStateOf("") }
//    var fechaNacimiento by remember { mutableStateOf("") }
//    var direccion by remember { mutableStateOf("") }
//    var unidadCodigo by remember { mutableStateOf("") }
//    var departamentoCodigo by remember { mutableStateOf("") }
//    var hospitalCodigo by remember { mutableStateOf("") }
//    val corrutineScope = rememberCoroutineScope()
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
//    ) {
//        item {
//            TextInputField(
//                value = numeroHistoriaClinica,
//                onValueChange = { numeroHistoriaClinica = it },
//                label = "Número de historia clínica"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(value = nombre, onValueChange = { nombre = it }, label = "Nombre")
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(value = apellidos, onValueChange = { apellidos = it }, label = "Apellidos")
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = fechaNacimiento,
//                onValueChange = { fechaNacimiento = it },
//                label = "Fecha de nacimiento"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextAreaField(value = direccion, onValueChange = { direccion = it }, label = "Dirección")
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(value = unidadCodigo, onValueChange = { unidadCodigo = it }, label = "Código de unidad")
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = departamentoCodigo,
//                onValueChange = { departamentoCodigo = it },
//                label = "Código de departamento"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = hospitalCodigo,
//                onValueChange = { hospitalCodigo = it },
//                label = "Código de hospital"
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            SubmitButton(
//
//                onClicked = {
//                    corrutineScope.launch {
//                        PatientDAO.crearPaciente(
//                            numeroHistoriaClinica,
//                            nombre,
//                            apellidos,
//                            LocalDate.parse(fechaNacimiento).toString(),
//                            direccion,
//                            unidadCodigo,
//                            departamentoCodigo,
//                            hospitalCodigo
//                        )
//                    }
//                }
//            )
//        }
//    }
//}