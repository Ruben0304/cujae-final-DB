package vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    var turnos by remember { mutableStateOf(listOf<Turno>()) }
    var consultas by remember { mutableStateOf(listOf<Consulta>()) }
    val coroutineScope = rememberCoroutineScope()



    fun performSearch(query: String) {
        coroutineScope.launch {
            isLoading = true
            // Simulación de demora en la red
            searchResults = SearchItems.allItems.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.id.contains(query, ignoreCase = true)
            }

            turnos = SearchItems.turnos.filter {
                it.doctor.contains(query, ignoreCase = true)

            }

            consultas = SearchItems.consultas.filter {
                it.doctor.contains(query, ignoreCase = true) ||
                        it.paciente.contains(query, ignoreCase = true)

            }
            delay(1000)
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
                    .height(1000.dp), // Hacer que el LazyColumn sea scroleable verticalmente
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
                if(searchResults.isNotEmpty()){
                    item {
                        Box(Modifier.height((turnos.size*70+15).dp)) {
                            TurnoTable(turnos)
                        }

                    }

                    item {
                        Box(Modifier.height((consultas.size*70+15).dp)) {
                            ConsultaTable(consultas)// Segunda tabla
                        }

                    }
                }
            }



        }
    }

}
