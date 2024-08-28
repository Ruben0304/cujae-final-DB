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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardContent() {


    var selectedFilter by remember { mutableStateOf("Unidad") }
    val filterOptions = listOf("Unidad", "Departamento", "Hospital")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF121212))
    ) {

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


        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            StatCard("Atendidos", "4", Icons.Outlined.CheckCircle, Color(0xFF42A5F5))
            Spacer(modifier = Modifier.width(16.dp))
            StatCard("No atendidos", "7", Icons.Outlined.ErrorOutline, Color(0xFFF44336))
            Spacer(modifier = Modifier.width(16.dp))
            StatCard(
                "Dados de Alta",
                "9",
                Icons.Outlined.LocalHospital,
                Color(0xFF4CAF50)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        // Nuevos componentes


        Row(
            modifier = Modifier
                .padding(16.dp)
        ) { AnimatedProgressCard("Pacientes atendidos", 0.8f, Color(0xff52ad6c))
//            Spacer(modifier = Modifier.width(10.dp))
            AnimatedProgressCard("Ocupación de Camas", 0.2f, Color(0xff4c6faf))
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



