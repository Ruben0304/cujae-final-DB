package vistas

import DialogButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dao.PatientDAO
import global.Global
import kotlinx.coroutines.launch
import modelos.PatientTable
import vistas.colores.patientGradient
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard


@Composable
fun PacientesHospital() {
    var pacientes by remember { mutableStateOf(listOf<PatientTable>()) }
    var isLoading by remember { mutableStateOf(true) }


    val coroutineScope = rememberCoroutineScope()

    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            if (Global.selectedHospital != null )
                pacientes = PatientDAO.getPacientesPorHospital(Global.selectedHospital!!)
            isLoading = false
        }
    }

    // Filtrar doctores
//    val filteredDoctors = pacientes.filter { paciente ->
//        (selectedHospital == null || paciente.apellidos == selectedHospital) &&
//                (selectedDepartamento == null || paciente.departamentoNombre == selectedDepartamento) &&
//                (selectedUnidad == null || paciente.unidadNombre == selectedUnidad)
//    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Listado de pacientes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )


        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 190.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(pacientes) { paciente ->
                    RotatingCard(
                        frontGradient = patientGradient,
                        labelText = "Paciente",
                        avatar = painterResource("Untitled.png"),
                        titleText = paciente.nombre + " " + paciente.apellidos,
                        subtitleText = paciente.numeroHistoriaClinica,
                        infoItems = listOf(
                            InfoItem(Icons.Rounded.Map, "Dirección", paciente.direccion),
                            InfoItem(Icons.Rounded.CalendarToday, "Fecha de Nacimiento", paciente.fechaNacimiento),
                        )
                    ){
                        GlassmorphismDialogManager.showDialog(
                            listOf(
                                DialogButton(
                                    "Aceptar",
                                    "✅"
                                ) { println("Aceptar clicked"); GlassmorphismDialogManager.hideDialog() },
                                DialogButton(
                                    "Cancelar",
                                    "❌"
                                ) { println("Cancelar clicked"); GlassmorphismDialogManager.hideDialog() },
                                DialogButton("Más información", "ℹ️") { println("Más información clicked") }
                            )
                        )
                    }
                }
            }
        }
    }
}

//fun generateDummyPatients(): List<Patient> {
//    return List(20) { index ->
//        Patient(
//            nombre = "Dr. Nombre ${index + 1}",
//            numeroHistoriaClinica = "ID${100 + index}",
//            apellidos = listOf("Hospital A", "Hospital B", "Hospital C").random(),
//            departamentoNombre = listOf("Cardiología", "Neurología", "Pediatría").random(),
//            unidadNombre = listOf("Unidad A", "Unidad B", "Unidad C").random(),
//            fechaNacimiento = "2003",
//             direccion = "166"
//        )
//    }
//}