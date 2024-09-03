package vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import dao.SearchDAO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import modelos.Consulta
import modelos.Turno
import vistas.util.Colores
import vistas.componentes.InfoItem
import vistas.componentes.RotatingCard
import vistas.util.SearchItems


data class SearchItem(
    val type: String,
    val name: String,
    val id: String,
    val gradient: List<Color>,
    val avatar: String,
    val infoItems: List<InfoItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var searchText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf(listOf<SearchItem>()) }
    var causas by remember { mutableStateOf(listOf<SearchDAO.Causa>()) }
    var consultas by remember { mutableStateOf(listOf<SearchDAO.Consulta>()) }
    val coroutineScope = rememberCoroutineScope()


    fun performSearch(query: String) {
        coroutineScope.launch {
            isLoading = true
            // Llamadas paralelas a los métodos de búsqueda
            // Llamadas a los métodos de búsqueda
            val causasDeferred = async { SearchDAO.buscarCausasNoAtencion(query) }
            val consultasDeferred = async { SearchDAO.buscarConsultas(query) }
            val hospitalesDeferred = async { SearchDAO.buscarHospitales(query) }
            val medicosDeferred = async { SearchDAO.buscarMedicos(query) }
            val pacientesDeferred = async { SearchDAO.buscarPacientes(query) }

            try {
                // Esperar a que todas las búsquedas terminen
                causas = causasDeferred.await()
                consultas = consultasDeferred.await()
                val hospitales = hospitalesDeferred.await()
                val medicos = medicosDeferred.await()
                val pacientes = pacientesDeferred.await()

                // Mapear los resultados de hospitales, médicos y pacientes a SearchItem
                searchResults = mapResultsToSearchItems(hospitales, medicos, pacientes)
            } catch (e: Exception) {
                // Manejo de errores
                println("Error en la búsqueda: ${e.message}")
            }

            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        vistas.componentes.SearchBar(
            searchText = searchText,
            onSearchTextChange = {
                searchText = it
                performSearch(it)
            },
            onSearchTriggered = { /* No se necesita aquí */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1000.dp), // Hacer que el LazyColumn sea scrollable verticalmente
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(), // Asegura que el LazyRow ocupe todo el ancho
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(searchResults) { item ->
                            RotatingCard(
                                frontGradient = item.gradient,
                                labelText = item.type,
                                avatar = painterResource(item.avatar),
                                titleText = item.name,
                                subtitleText = item.id,
                                infoItems = item.infoItems,
                            )
                        }
                    }
                }
                if (searchResults.isNotEmpty()) {
                    item {
                        Box(Modifier.height(300.dp)) {
                            CausasTable(causas)
                        }
                    }

                    item {
                        Box(Modifier.height(300.dp)) {
                            ConsultaTable(consultas) // Segunda tabla
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CausasTable(causas: List<SearchDAO.Causa>) {
    val surfaceColor = Color(0xFF2D2D2D)
    val headerColor = Color(0xFF3700B3)
    val textColor = Color.White
    val dividerColor = Color(0xFF3D3D3D)

    Surface(
        color = surfaceColor,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                listOf("ID", "Descripción", "").forEach { header ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = header,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }

            // Rows
            LazyColumn {
                items(causas) { causa ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) { Text(causa.causa_id.toString(), color = textColor) }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) { Text(causa.descripcion, color = textColor) }

                            // Columna vacía para mantener el diseño
                            Box(
                                modifier = Modifier.weight(1f),

                                )
                        }
                        if (causa != causas.last()) {
                            Divider(color = dividerColor, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConsultaTable(consultas: List<SearchDAO.Consulta>) {
    val surfaceColor = Color(0xFF2D2D2D)
    val headerColor = Color(0xFF3700B3)
    val textColor = Color.White
    val dividerColor = Color(0xFF3D3D3D)

    Surface(
        color = surfaceColor,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                listOf("ID", "Fecha/Hora", "Turno", "Historia Clínica").forEach { header ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = header,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }

            // Rows
            LazyColumn {
                items(consultas) { consulta ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) { Text(consulta.consulta_id.toString(), color = textColor) }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) { Text(consulta.fecha_hora, color = textColor) }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) { Text(consulta.turno_numero.toString(), color = textColor) }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) { Text(consulta.paciente_numero_historia_clinica, color = textColor) }
                        }
                        if (consulta != consultas.last()) {
                            Divider(color = dividerColor, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

fun mapResultsToSearchItems(
    hospitales: List<SearchDAO.Hospital>,
    medicos: List<SearchDAO.Medico>,
    pacientes: List<SearchDAO.Paciente>
): List<SearchItem> {
    return buildList {
        // Mapeo de hospitales
        addAll(hospitales.map {
            SearchItem(
                type = "Hospital",
                name = it.hospital_nombre,
                id = it.hospital_codigo,
                gradient = Colores.hospitalGradient,
                avatar = "Hospital Sign.png",
                infoItems = listOf(
//                    InfoItem(Icons.Rounded.Badge, "Número de Historia", "67890"),
//                    InfoItem(Icons.Rounded.Phone, "Teléfono", "555-5678"),
//                    InfoItem(Icons.Rounded.CalendarToday, "Fecha de Fundación", "01/01/1900"),
//                    InfoItem(Icons.Rounded.Email, "Correo Electrónico", "hospital@example.com")
                )
            )
        })

        // Mapeo de médicos
        addAll(medicos.map {
            SearchItem(
                type = "Doctor",
                name = "${it.medico_nombre} ${it.medico_apellidos}",
                id = it.especialidad,
                gradient = Colores.doctorGradient,
                avatar = "a.jpg",
                infoItems = listOf(
                    InfoItem(Icons.Rounded.Badge, "Codigo", it.medico_codigo),
                )
            )
        })

        // Mapeo de pacientes
        addAll(pacientes.map {
            SearchItem(
                type = "Paciente",
                name = "${it.paciente_nombre} ${it.paciente_apellidos}",
                id = it.paciente_numero_historia_clinica,
                gradient = Colores.patientGradient,
                avatar = "Untitled.png",
                infoItems = listOf(
                    InfoItem(Icons.Rounded.Map, "Dirección", it.direccion),
                )
            )
        })
    }
}
