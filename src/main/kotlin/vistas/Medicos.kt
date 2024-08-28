package vistas

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import modelos.Doctor
import vistas.util.Colores
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard
import vistas.componentes.SelectInputFieldFiltrado
import kotlin.random.Random


@Composable
fun DoctorListContent() {
    var doctors by remember { mutableStateOf(listOf<Doctor>()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedHospital by remember { mutableStateOf<String?>(null) }
    var selectedDepartamento by remember { mutableStateOf<String?>(null) }
    var selectedUnidad by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            delay(1000) // Simular delay de 1 segundo
            doctors = generateDummyDoctors()
            isLoading = false
        }
    }

    // Filtrar doctores
    val filteredDoctors = doctors.filter { doctor ->
        (selectedHospital == null || doctor.apellidos == selectedHospital) &&
                (selectedDepartamento == null || doctor.especialidad == selectedDepartamento) &&
                (selectedUnidad == null || doctor.telefono == selectedUnidad)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Listado de médicos",
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
                items(filteredDoctors) { doctor ->
                    RotatingCard(
                        frontGradient = Colores.doctorGradient,
                        labelText = "Doctor",
                        avatar = painterResource("a.jpg"),
                        titleText = doctor.nombre,
                        subtitleText = doctor.numeroLicencia,
                        infoItems = listOf(
                            InfoItem(Icons.Rounded.Badge, "Número de Historia", "67890"),
                            InfoItem(Icons.Rounded.Phone, "Teléfono", "555-5678"),
                            InfoItem(Icons.Rounded.CalendarToday, "Fecha de Nacimiento", "01/01/1990"),
                            InfoItem(Icons.Rounded.Email, "Correo Electrónico", "paciente@example.com")
                        )
                    )
                }
            }
        }
    }
}

fun generateDummyDoctors(): List<Doctor> {
    return List(20) { index ->
        Doctor(
            nombre = "Dr. Nombre ${index + 1}",
            numeroLicencia = "ID${100 + index}",
            apellidos = listOf("Hospital A", "Hospital B", "Hospital C").random(),
            especialidad = listOf("Cardiología", "Neurología", "Pediatría").random(),
            telefono = listOf("Unidad A", "Unidad B", "Unidad C").random(),
            aniosExperiencia = Random.nextInt(),
            datosContacto = Random.nextFloat().toString()
        )
    }
}





