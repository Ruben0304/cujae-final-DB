import androidx.compose.desktop.ui.tooling.preview.Preview
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import modelos.Hospital
import vistas.util.Colores
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HospitalListContent() {
    var hospitales by remember { mutableStateOf(listOf<Hospital>()) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            delay(1000) // Simular delay de 1 segundo
            hospitales = generateDummyHospital()
            isLoading = false
        }
    }



    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Listado de hospitales",
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
                items(hospitales) { hospital ->
                    RotatingCard(
                        frontGradient = Colores.hospitalGradient,
                        labelText = "Hospital",
                        avatar = painterResource("Hospital Sign.png"),
                        titleText = hospital.nombre,
                        subtitleText = hospital.codigo,
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

fun generateDummyHospital(): List<Hospital> {
    return List(20) { index ->
        Hospital(
            nombre = "Dr. Nombre ${index + 1}",
            codigo = "9606960"
        )
    }
}