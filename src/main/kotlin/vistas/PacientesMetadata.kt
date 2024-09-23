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
import auth.Auth
import dao.PatientDAO
import dao.RegistroDAO
import global.Global
import kotlinx.coroutines.launch
import modelos.PatientTable
import modelos.Registro
import vistas.componentes.*
import vistas.nav.NavManager


@Composable
fun PatientInfo() {
    var pacientes by remember { mutableStateOf(listOf<PatientTable>()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        pacientes = PatientDAO.getPacientes()
        isLoading = false
    }

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
                    val frontGradient = listOf(Color(0xFF388E3C), Color(0xFF2E7D32))
                    var items = mutableListOf(
                        InfoItem(Icons.Rounded.Map, "Dirección", paciente.direccion),
                        InfoItem(Icons.Rounded.CalendarMonth, "Fecha de nacimiento", paciente.fechaNacimiento),
                    )

                    RotatingCard(
                        frontGradient = frontGradient,
                        labelText = "Paciente",
                        avatar = painterResource("Untitled.png"),
                        titleText = paciente.nombre + " " + paciente.apellidos,
                        subtitleText = paciente.ci,
                        infoItems = items
                    ) {

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