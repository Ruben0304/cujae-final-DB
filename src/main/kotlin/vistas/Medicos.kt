import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dao.DepartamentoDAO
import dao.DoctorDAO
import dao.HospitalDAO
import dao.UnidadDAO
import kotlinx.coroutines.launch
import modelos.Departamento
import modelos.Doctor
import modelos.Hospital
import modelos.Unidad
import vistas.FilterDropdown

const val PAGE_SIZE = 20 // Número de elementos por página


@Preview
@Composable
fun DoctorListContent() {
    var doctors by remember { mutableStateOf(listOf<Doctor>()) }
    var isLoading by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(0) }
    var hasMoreData by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var selectedHospital by remember { mutableStateOf<String?>(null) }
    var selectedDepartamento by remember { mutableStateOf<String?>(null) }
    var selectedUnidad by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo

    // Cargar los datos iniciales
    LaunchedEffect(selectedHospital, selectedDepartamento, selectedUnidad) {
        isLoading = true
        currentPage = 0
        loadMoreDoctors(
            currentPage,
            selectedHospital,
            selectedDepartamento,
            selectedUnidad,
            emptyList()
        ) { newDoctors, hasMore ->
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
                        loadMoreDoctors(
                            currentPage,
                            selectedHospital,
                            selectedDepartamento,
                            selectedUnidad,
                            doctors
                        ) { newDoctors, hasMore ->
                            doctors = newDoctors
                            hasMoreData = hasMore
                            isLoading = false
                        }
                    }
                }
            }
    }

    val darkColorScheme = darkColorScheme(
        primary = Color(104, 151, 187),
        surface = Color(0xFF1E1E1E),
        background = Color(0xFF121212),
        onSurface = Color(0xFFE0E0E0),
        onBackground = Color(0xFFE0E0E0)
    )

    MaterialTheme(colorScheme = darkColorScheme) {
        Scaffold(
            topBar = {
                Header(
                    onHospitalSelected = { selectedHospital = it.toString() },
                    onDepartamentoSelected = { selectedDepartamento = it.toString() },
                    onUnidadSelected = { selectedUnidad = it.toString() },
                    onSearch = { query ->
                        // Manejar la búsqueda aquí
                        println("Buscando: $query")
                        // Realizar la búsqueda con la consulta
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
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

                // Oscurecer el fondo si el diálogo está abierto
                if (showDialog) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    FloatingActionButtonWithDialog(
                        onSubmit = {
                            // Acción al enviar el formulario
                            println("Formulario enviado")
                            showDialog = false
                        },
                        onDismiss = {
                            // Acción al cancelar el formulario
                            println("Formulario cancelado")
                            showDialog = false
                        },

                    )
                }
            }
        }
    }
}


suspend fun loadMoreDoctors(
    page: Int,
    hospitalId: String?,
    departamentoId: String?,
    unidadId: String?,
    currentDoctors: List<Doctor>,
    callback: (List<Doctor>, Boolean) -> Unit
) {
    val newDoctors = DoctorDAO.getFilteredDoctors(
        hospitalId = hospitalId , // Asume que siempre hay un hospital seleccionado
        departamentoId = departamentoId,
        unidadId = unidadId,
        offset = page * PAGE_SIZE,
        limit = PAGE_SIZE
    )
    val updatedDoctors = currentDoctors + newDoctors
    callback(updatedDoctors, newDoctors.size == PAGE_SIZE)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    onHospitalSelected: (String?) -> Unit,
    onDepartamentoSelected: (String?) -> Unit,
    onUnidadSelected: (String?) -> Unit,
    onSearch: (String) -> Unit
) {
    var selectedHospital by remember { mutableStateOf<String?>(null) }
    var selectedDepartamento by remember { mutableStateOf<String?>(null) }
    var selectedUnidad by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    var hospitals by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var departamentos by remember { mutableStateOf<List<Departamento>>(emptyList()) }
    var unidades by remember { mutableStateOf<List<Unidad>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()

    // Cargar hospitales al inicio
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            hospitals = HospitalDAO.getAllHospitals()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = { Text("Listado de Médicos") },
            actions = {
//                SimpleSearchBar(
//                    query = searchQuery,
//                    onQueryChange = {
//                        searchQuery = it
//                        onSearch(it)
//                    },
//                    onSearch = { onSearch(searchQuery) },
//                    modifier = Modifier.width(240.dp)
//                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterDropdown(
                label = "Hospital",
                options = listOf(1,2,3),
                selectedOption = selectedHospital?.toIntOrNull(),
                onOptionSelected = {
                    val hospitalCode = if (it == -1) null else it.toString()
                    selectedHospital = hospitalCode
                    selectedDepartamento = null
                    selectedUnidad = null
                    onHospitalSelected(hospitalCode)
                    if (hospitalCode != null) {
                        coroutineScope.launch {
                            departamentos = DepartamentoDAO.obtenerDepartamentosPorHospital(hospitalCode)
                        }
                    } else {
                        departamentos = emptyList()
                    }
                    unidades = emptyList()
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterDropdown(
                label = "Departamento",
                options = listOf(-1),
                selectedOption = selectedDepartamento?.toIntOrNull(),
                onOptionSelected = {
                    val departamentoCode = if (it == -1) null else it.toString()
                    selectedDepartamento = departamentoCode
                    selectedUnidad = null
                    onDepartamentoSelected(departamentoCode)
                    if (departamentoCode != null && selectedHospital != null) {
                        coroutineScope.launch {
                            unidades = UnidadDAO.obtenerUnidadesPorHospitalYDepartamento(selectedHospital!!, departamentoCode, 0, 100)
                        }
                    } else {
                        unidades = emptyList()
                    }
                },
                enabled = selectedHospital != null,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterDropdown(
                label = "Unidad",
                options = listOf(-1) ,
                selectedOption = selectedUnidad?.toIntOrNull(),
                onOptionSelected = {
                    val unidadCode = if (it == -1) null else it.toString()
                    selectedUnidad = unidadCode
                    onUnidadSelected(unidadCode)
                },
                enabled = selectedDepartamento != null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SimpleSearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit,
//    onSearch: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    TextField(
//        value = query,
//        onValueChange = onQueryChange,
//        placeholder = { Text("Buscar") },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Icono de búsqueda"
//            )
//        },
//        singleLine = true,
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
//        colors = TextFieldDefaults.colors(
////            disabledContainerColor = MaterialTheme.colorScheme.surface,
//            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
//        ),
//        modifier = modifier
//    )
//}


@Composable
fun DoctorDetails(doctor: Doctor) {
    Text(
        text = "Información adicional sobre el doctor ${doctor.nombre}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DoctorDetailItem(Icons.Rounded.Badge, "Número de Licencia", doctor.numeroLicencia)
        DoctorDetailItem(Icons.Rounded.Phone, "Teléfono", doctor.telefono ?: "N/A")
        DoctorDetailItem(Icons.Rounded.Star, "Años de Experiencia", doctor.aniosExperiencia?.toString() ?: "N/A")
        DoctorDetailItem(Icons.Rounded.Email, "Datos de Contacto", doctor.datosContacto ?: "N/A")
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorItem(doctor: Doctor) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .animateContentSize(),
        onClick = { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = Color(28, 28, 28)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        doctor.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        doctor.especialidad,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row {
                    IconButton(onClick = { /* TODO: Implement update action */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Update",
                            tint = Color(84, 138, 247)
                        )
                    }
                    IconButton(onClick = { /* TODO: Implement delete action */ }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(201, 79, 79)
                        )
                    }
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

