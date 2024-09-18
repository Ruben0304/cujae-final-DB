//package vistas.form.edit
//
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import dao.ConsultaDAO
//import kotlinx.coroutines.launch
//import vistas.componentes.SubmitButton
//import vistas.componentes.TextInputField
//import java.sql.Timestamp
//
//@Composable
//fun EditConsultaForm() {
//    var turnoNumero by remember { mutableStateOf("") }
//    var turnoUnidadCodigo by remember { mutableStateOf("") }
//    var turnoDepartamentoCodigo by remember { mutableStateOf("") }
//    var turnoHospitalCodigo by remember { mutableStateOf("") }
//    var pacienteNumeroHistoriaClinica by remember { mutableStateOf("") }
//    var pacienteUnidadCodigo by remember { mutableStateOf("") }
//    var pacienteDepartamentoCodigo by remember { mutableStateOf("") }
//    var pacienteHospitalCodigo by remember { mutableStateOf("") }
//    var fechaHora by remember { mutableStateOf("") }
//    val corrutineScope = rememberCoroutineScope()
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
//    ) {
//        item {
//            TextInputField(value = turnoNumero, onValueChange = { turnoNumero = it }, label = "Número de turno")
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = turnoUnidadCodigo,
//                onValueChange = { turnoUnidadCodigo = it },
//                label = "Código de unidad de turno"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = turnoDepartamentoCodigo,
//                onValueChange = { turnoDepartamentoCodigo = it },
//                label = "Código de departamento de turno"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = turnoHospitalCodigo,
//                onValueChange = { turnoHospitalCodigo = it },
//                label = "Código de hospital de turno"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = pacienteNumeroHistoriaClinica,
//                onValueChange = { pacienteNumeroHistoriaClinica = it },
//                label = "Número de historia clínica del paciente"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = pacienteUnidadCodigo,
//                onValueChange = { pacienteUnidadCodigo = it },
//                label = "Código de unidad del paciente"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = pacienteDepartamentoCodigo,
//                onValueChange = { pacienteDepartamentoCodigo = it },
//                label = "Código de departamento del paciente"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(
//                value = pacienteHospitalCodigo,
//                onValueChange = { pacienteHospitalCodigo = it },
//                label = "Código de hospital del paciente"
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            TextInputField(value = fechaHora, onValueChange = { fechaHora = it }, label = "Fecha y hora")
//            Spacer(modifier = Modifier.height(24.dp))
//            SubmitButton(
//                onClicked = {
//                    corrutineScope.launch {
//                        ConsultaDAO.crearConsulta(
//                            turnoNumero.toInt(),
//                            turnoUnidadCodigo,
//                            turnoDepartamentoCodigo,
//                            turnoHospitalCodigo,
//                            pacienteNumeroHistoriaClinica,
//                            pacienteUnidadCodigo,
//                            pacienteDepartamentoCodigo,
//                            pacienteHospitalCodigo,
//                            Timestamp.valueOf(fechaHora).toString()
//                        )
//                    }
//                }
//            )
//        }
//    }
//}