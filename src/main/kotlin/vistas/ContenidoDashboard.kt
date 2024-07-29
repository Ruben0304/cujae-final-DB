package vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardContent(searchText: String, onSearchChange: (String) -> Unit) {


    // Example data for the process summary
    val processSummary = ProcessSummary(
        hospital = "Hospital A",
        department = "Departamento A",
        unit = "Unidad A",
        shiftNumber = "Turno 1",
        reportTime = "10:00 AM",
        reportDate = "2024-06-17",
        initialPatients = 100,
        attendedPatients = 80,
        totalPatients = 100,
        attendedPercentage = 80.0,
        unattendedPatients = 20,
        dischargedPatients = 10,
        abroadUnattended = 2,
        outOfProvinceUnattended = 3,
        otherUnitUnattended = 5,
        otherCausesUnattended = 8,
        unknownCausesUnattended = 2
    )


    var selectedFilter by remember { mutableStateOf("Unidad") }
    val filterOptions = listOf("Unidad", "Departamento", "Hospital")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF121212))
    ) {
        // Search Field
        TextField(
            value = searchText,
            onValueChange = onSearchChange,
            label = { Text("Buscar") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFF1E1E1E),
                focusedIndicatorColor = Color(0xFF1E88E5),
                unfocusedIndicatorColor = Color(0xFF1E88E5),
                textColor = Color.White,
                cursorColor = Color.White,
                focusedLabelColor = Color(0xFF1E88E5),
                unfocusedLabelColor = Color.Gray
            ),
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Filter Dropdown
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Filtrar por:", color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedFilter,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expandir",
                            tint = Color.White
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        backgroundColor = Color(0xFF1E1E1E),
                        textColor = Color.White

                    ),
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF1E1E1E))
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    filterOptions.forEach { filter ->
                        DropdownMenuItem(onClick = {
                            selectedFilter = filter
                            expanded = false
                        }) {
                            Text(filter, color = Color.Black)
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))


        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard("Atendidos", processSummary.attendedPatients.toString(), Icons.Outlined.CheckCircle, Color(0xFF42A5F5))
            Spacer(modifier = Modifier.width(16.dp))
            StatCard("No atendidos", processSummary.unattendedPatients.toString(), Icons.Outlined.ErrorOutline, Color(0xFFF44336))
            Spacer(modifier = Modifier.width(16.dp))
            StatCard(
                "Dados de Alta",
                processSummary.dischargedPatients.toString(),
                Icons.Outlined.LocalHospital,
                Color(0xFF4CAF50)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Header
        Text(
            "Resumen del Proceso",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        // Dashboard Cards
        Column {
            Card(
                backgroundColor = Color(0xFF1E1E1E),
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Hospital: ${processSummary.hospital}", color = Color.White)
                    Text("Departamento: ${processSummary.department}", color = Color.White)
                    Text("Unidad: ${processSummary.unit}", color = Color.White)
                    Text("Número del Turno: ${processSummary.shiftNumber}", color = Color.White)
                    Text("Hora del Informe: ${processSummary.reportTime}", color = Color.White)
                    Text(
                        "Cantidad de Pacientes al Inicio de las Consultas: ${processSummary.initialPatients}",
                        color = Color.White
                    )
                    Text("Cantidad de Pacientes Atendidos: ${processSummary.attendedPatients}", color = Color.White)
                    Text("Cantidad de Pacientes en Total: ${processSummary.totalPatients}", color = Color.White)
                    Text(
                        "Porcentaje de Pacientes Atendidos: ${processSummary.attendedPercentage}%",
                        color = Color.White
                    )
                    Text(
                        "Cantidad de Pacientes que No Fueron Atendidos: ${processSummary.unattendedPatients}",
                        color = Color.White
                    )
                    Text(
                        "Cantidad de Pacientes Dados de Alta: ${processSummary.dischargedPatients}",
                        color = Color.White
                    )
                    Text(
                        "Pacientes No Atendidos por Estar en el Extranjero: ${processSummary.abroadUnattended}",
                        color = Color.White
                    )
                    Text(
                        "Pacientes No Atendidos por Estar Fuera de la Provincia: ${processSummary.outOfProvinceUnattended}",
                        color = Color.White
                    )
                    Text(
                        "Pacientes No Atendidos por Estar Hospitalizados en Otra Unidad: ${processSummary.otherUnitUnattended}",
                        color = Color.White
                    )
                    Text(
                        "Pacientes No Atendidos por Otras Causas: ${processSummary.otherCausesUnattended}",
                        color = Color.White
                    )
                    Text(
                        "Pacientes No Atendidos y Se Desconocen las Causas: ${processSummary.unknownCausesUnattended}",
                        color = Color.White
                    )
                }
            }
        }
    }

}


// Data classes for the example data

data class ProcessSummary(
    val hospital: String,
    val department: String,
    val unit: String,
    val shiftNumber: String,
    val reportTime: String,
    val reportDate: String,
    val initialPatients: Int,
    val attendedPatients: Int,
    val totalPatients: Int,
    val attendedPercentage: Double,
    val unattendedPatients: Int,
    val dischargedPatients: Int,
    val abroadUnattended: Int,
    val outOfProvinceUnattended: Int,
    val otherUnitUnattended: Int,
    val otherCausesUnattended: Int,
    val unknownCausesUnattended: Int
)