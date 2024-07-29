import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import modelos.Patient
import modelos.Entities

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PatientListContent() {
    var patients by remember { mutableStateOf(listOf<Patient>()) }
    var isLoading by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(0) }
    var hasMoreData by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        isLoading = true
        loadMorePatients(currentPage, patients) { newPatients, hasMore ->
            patients = newPatients
            hasMoreData = hasMore
            isLoading = false
        }
    }

    // Monitorear cuando se llega al final de la lista
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { visibleItem ->
                if (visibleItem != null && visibleItem.index >= patients.size - 1 && !isLoading && hasMoreData) {
                    coroutineScope.launch {
                        isLoading = true
                        currentPage++
                        loadMorePatients(currentPage, patients) { newPatients, hasMore ->
                            patients = newPatients
                            hasMoreData = hasMore
                            isLoading = false
                        }
                    }
                }
            }
    }

    val darkColorScheme = darkColorScheme(
        primary = Color(0xFF64B5F6),
        surface = Color(0xFF1E1E1E),
        background = Color(0xFF121212),
        onSurface = Color(0xFFE0E0E0),
        onBackground = Color(0xFFE0E0E0)
    )

    MaterialTheme(colorScheme = darkColorScheme) {
        Scaffold(
            topBar = { PatientHeader() },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            LazyColumn(
                state = listState,
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(patients.sortedBy { it.name }) { patient ->
                    PatientItem(patient)
                }

                item {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

suspend fun loadMorePatients(
    page: Int,
    currentPatients: List<Patient>,
    callback: (List<Patient>, Boolean) -> Unit
) {
    val newPatients = Entities.getPatients(page * PAGE_SIZE, PAGE_SIZE)
    val updatedPatients = currentPatients + newPatients
    callback(updatedPatients, newPatients.size == PAGE_SIZE)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHeader() {
    TopAppBar(
        title = {
            Text(
                "Listado de Pacientes",
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientItem(patient: Patient) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        onClick = { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource("patient_avatar.png"),
                    contentDescription = "Patient Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${patient.name} ${patient.lastName}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "ID: ${patient.clinicalRecordNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                PatientDetails(patient)
            }
        }
    }
}

@Composable
fun PatientDetails(patient: Patient) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PatientDetailItem(Icons.Rounded.LocalHospital, "Hospital", patient.hospitalName)
        PatientDetailItem(Icons.Rounded.Business, "Departamento", patient.departmentName)
        PatientDetailItem(Icons.Rounded.Group, "Unidad", patient.unitName)
        PatientDetailItem(Icons.Rounded.Badge, "Número de Historia Clínica", patient.clinicalRecordNumber)
        PatientDetailItem(Icons.Rounded.Cake, "Fecha de Nacimiento", patient.birthDate)
        PatientDetailItem(Icons.Rounded.Home, "Dirección", patient.address ?: "N/A")
    }
}

@Composable
fun PatientDetailItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}