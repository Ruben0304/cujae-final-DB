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
import modelos.Doctor
import modelos.Entities

const val PAGE_SIZE = 20 // Número de elementos por página

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DoctorListContent() {
    var doctors by remember { mutableStateOf(listOf<Doctor>()) }
    var isLoading by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(0) }
    var hasMoreData by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Cargar los datos iniciales
    LaunchedEffect(Unit) {
        isLoading = true
        loadMoreDoctors(currentPage, doctors) { newDoctors, hasMore ->
            doctors = newDoctors
            hasMoreData = hasMore
            isLoading = false
        }
    }

    // Monitorear cuando se llega al final de la lista
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { visibleItem ->
                if (visibleItem != null && visibleItem.index >= doctors.size - 1 && !isLoading && hasMoreData) {
                    coroutineScope.launch {
                        isLoading = true
                        currentPage++
                        loadMoreDoctors(currentPage, doctors) { newDoctors, hasMore ->
                            doctors = newDoctors
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
            topBar = { Header() },
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
                items(doctors) { doctor ->
                    DoctorItem(doctor)
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

suspend fun loadMoreDoctors(
    page: Int,
    currentDoctors: List<Doctor>,
    callback: (List<Doctor>, Boolean) -> Unit
) {
    val newDoctors = Entities.getDoctors(page * PAGE_SIZE, PAGE_SIZE)
    val updatedDoctors = currentDoctors + newDoctors
    callback(updatedDoctors, newDoctors.size == PAGE_SIZE)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header() {
    TopAppBar(
        title = {
            Text(
                "Listado de Médicos",
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
fun LoadingAnimation() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorItem(doctor: Doctor) {
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
                    painter = painterResource("doctor_avatar.png"),
                    contentDescription = "Doctor Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        doctor.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        doctor.specialty,
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
                DoctorDetails(doctor)
            }
        }
    }
}

@Composable
fun DoctorDetails(doctor: Doctor) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DoctorDetailItem(Icons.Rounded.LocalHospital, "Hospital", doctor.hospitalName)
        DoctorDetailItem(Icons.Rounded.Business, "Departamento", doctor.departmentName)
        DoctorDetailItem(Icons.Rounded.Group, "Unidad", doctor.unitName)
        DoctorDetailItem(Icons.Rounded.Badge, "Número de Licencia", doctor.licenseNumber)
        DoctorDetailItem(Icons.Rounded.Phone, "Teléfono", doctor.phone ?: "N/A")
        DoctorDetailItem(Icons.Rounded.Star, "Años de Experiencia", doctor.experience.toString())
        DoctorDetailItem(Icons.Rounded.Email, "Datos de Contacto", doctor.contactInfo ?: "N/A")
    }
}

@Composable
fun DoctorDetailItem(icon: ImageVector, label: String, value: String) {
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