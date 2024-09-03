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
import androidx.compose.ui.zIndex
import dao.PatientDAO
import global.Global
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import modelos.Patient
import modelos.Registro
import vistas.util.Colores
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard
import vistas.componentes.SelectInputFieldFiltrado


@Composable
fun PatientListContent(unidadCodigo:String, departamentoCodigo: String) {
    var pacientes by remember { mutableStateOf(listOf<Registro>()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedHospital by remember { mutableStateOf<String?>(null) }
    var selectedDepartamento by remember { mutableStateOf<String?>(null) }
    var selectedUnidad by remember { mutableStateOf<String?>(null) }
    val estadoColores = mapOf(
        "pendiente" to listOf(Color(0xFFFF7043), Color(0xFFF4511E)),   // Tonalidades más oscuras de naranja
        "atendido" to listOf(Color(0xFF388E3C), Color(0xFF2E7D32)),    // Tonalidades más oscuras de verde
        "no atendido" to listOf(Color(0xFFC62828), Color(0xFFB71C1C))  // Tonalidades más oscuras de rojo
    )


    val coroutineScope = rememberCoroutineScope()

    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            if (Global.selectedHospital != null )
                  pacientes = PatientDAO.obtenerPacientesConEstadoYCausa(unidadCodigo,departamentoCodigo,Global.selectedHospital)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(2f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SelectInputFieldFiltrado(
                    value = selectedHospital ?: "Seleccionar",
                    onValueChange = {
                        selectedHospital = it
                        selectedDepartamento = null
                        selectedUnidad = null
                    },
                    label = "Hospital",
                    options = listOf("Hospital A", "Hospital B", "Hospital C"),
                    enabled = true
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Box(modifier = Modifier.weight(1f)) {
                SelectInputFieldFiltrado(
                    value = selectedDepartamento ?: "Seleccionar",
                    onValueChange = {
                        selectedDepartamento = it
                        selectedUnidad = null
                    },
                    label = "Departamento",
                    options = listOf("Cardiología", "Neurología", "Pediatría"),
                    enabled = selectedHospital != null
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Box(modifier = Modifier.weight(1f)) {
                SelectInputFieldFiltrado(
                    value = selectedUnidad ?: "Seleccionar",
                    onValueChange = { selectedUnidad = it },
                    label = "Unidad",
                    options = listOf("Unidad A", "Unidad B", "Unidad C"),
                    enabled = selectedHospital != null && selectedDepartamento != null
                )
            }
        }

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
                    val frontGradient = when (paciente.estado) {
                        "pendiente" -> estadoColores["pendiente"]!!
                        "atendido" -> estadoColores["atendido"]!!
                        "no atendido" -> estadoColores["no atendido"]!!
                        else -> listOf(Color.Gray, Color.DarkGray)  // Color por defecto si no coincide
                    }
                    var items = mutableListOf<InfoItem>(
                        InfoItem(Icons.Rounded.CalendarMonth, "Fecha de nacimiento", paciente.fecha_nacimiento) ,
                        InfoItem(Icons.Rounded.Map, "Dirección", paciente.direccion),
                    )
                    if(paciente.estado == "no atendido" && paciente.causa_no_atencion != null)
                        items.add( InfoItem(Icons.Rounded.Map, "", paciente.causa_no_atencion),)

                    RotatingCard(
                        frontGradient =  frontGradient,
                        labelText = paciente.estado.replaceFirstChar { it.uppercase() },
                        avatar = painterResource("Untitled.png"),
                        titleText = paciente.nombre + " " + paciente.apellidos,
                        subtitleText = paciente.numero_historia_clinica,
                        infoItems = items
                    )
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