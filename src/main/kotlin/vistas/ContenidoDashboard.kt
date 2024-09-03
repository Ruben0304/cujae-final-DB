package vistas

import vistas.componentes.AnimatedProgressCard
import vistas.componentes.StatCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dao.CantidadesDAO.obtenerConteoPacientesPorEstado
import dao.DoctorDAO
import dao.PatientDAO
import global.Global
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardContent() {
    val coroutineScope = rememberCoroutineScope()

    var noAtendidos by remember { mutableStateOf(0) }
    var atendidos by remember { mutableStateOf(0) }
    var fallecidos by remember { mutableStateOf(0) }
    var pendientes by remember { mutableStateOf(0) }
    var dadosDeAlta by remember { mutableStateOf(0) }
    var cantidadDepartamentos by remember { mutableStateOf(0) }
    var cantidadUnidades by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }


//    var selectedFilter by remember { mutableStateOf("Unidad") }
//    val filterOptions = listOf("Unidad", "Departamento", "Hospital")
//    var expanded by remember { mutableStateOf(false) }

    // Simular carga de doctores
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            if (Global.selectedHospital != null) {
                val conteoPacientes = obtenerConteoPacientesPorEstado(Global.selectedHospital)

                noAtendidos = conteoPacientes.no_atendidos
                atendidos = conteoPacientes.atendidos
                fallecidos = conteoPacientes.fallecidos
                pendientes = conteoPacientes.pendientes
                dadosDeAlta = conteoPacientes.dados_de_alta
                cantidadDepartamentos = conteoPacientes.cantidad_departamentos
                cantidadUnidades = conteoPacientes.cantidad_unidades
            }
            isLoading = false
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF121212))
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {

            // Filter Dropdown
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
//            Text("Filtrar por:", color = Color.White)
//            Spacer(modifier = Modifier.width(8.dp))
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded }
//            ) {
//                TextField(
//                    value = selectedFilter,
//                    onValueChange = {},
//                    readOnly = true,
//                    trailingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.ArrowDropDown,
//                            contentDescription = "Expandir",
//                            tint = Color.White
//                        )
//                    },
//                    colors = ExposedDropdownMenuDefaults.textFieldColors(
//                        backgroundColor = Color(0xFF1E1E1E),
//                        textColor = Color.White
//
//                    ),
//                    modifier = Modifier.fillMaxWidth().background(Color(0xFF1E1E1E))
//                )
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false },
//                ) {
//                    filterOptions.forEach { filter ->
//                        DropdownMenuItem(onClick = {
//                            selectedFilter = filter
//                            expanded = false
//                        }) {
//                            Text(filter, color = Color.Black)
//                        }
//                    }
//                }
//            }
            }
//
//
//        Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                StatCard("Unidades", cantidadUnidades.toString(), Icons.Outlined.CheckCircle, Color(0xFF42A5F5))
                Spacer(modifier = Modifier.width(16.dp))


                StatCard(
                    "Departamentos",
                    cantidadDepartamentos.toString(),
                    Icons.Outlined.ErrorOutline,
                    Color(0xFFF44336)
                )

                Spacer(modifier = Modifier.width(16.dp))
                StatCard(
                    "Pacientes no atendidos",
                    noAtendidos.toString(),
                    Icons.Outlined.LocalHospital,
                    Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            // Nuevos componentes

            if (noAtendidos != 0)
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    AnimatedProgressCard(
                        "Vivos restantes",
                        1f - fallecidos.toFloat() / (noAtendidos.toFloat() + atendidos.toFloat()),
                        Color(0xff52ad6c)
                    )
//            Spacer(modifier = Modifier.width(10.dp))
                    AnimatedProgressCard(
                        "Muertos",
                        fallecidos.toFloat() / (noAtendidos.toFloat() + atendidos.toFloat()),
                        Color(0xff73122c)
                    )
                }

//        FloatingActionButtonWithMenu()


//        PdfGeneratorCard()
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        AnimatedDataList(listOf("Paciente 1", "Paciente 2", "Paciente 3"))
//
//        FloatingActionButtonWithMenu()

        }
    }

}



