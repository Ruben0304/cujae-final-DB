package vistas

import DoctorListContent
import HospitalListContent
import PatientListContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.sharp.Dashboard
import androidx.compose.material.icons.sharp.LocalHospital
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import modelos.Entities
import modelos.Hospital

@Preview
@Composable
fun DashboardApp() {
    var selectedTab by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }

    

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        // Top App Bar
        TopAppBar(
            title = { Text("Gestión Hospitalaria", color = Color.White) },
            backgroundColor = Color(0xFF1E88E5),
            actions = {
                IconButton(onClick = { /* Search Action */ }) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                }
            }
        )

        // Body
        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .background(Color(0xFF1C1C1C))
                    .padding(8.dp)
            ) {
                Text(
                    "Menu",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
                SideBarItem("Dashboard", Icons.Sharp.Dashboard, selectedTab == 0) { selectedTab = 0 }
                SideBarItem("Listado de Pacientes", Icons.Outlined.Sick, selectedTab == 1) { selectedTab = 1 }
                SideBarItem("Listado de Médicos", Icons.Outlined.People, selectedTab == 2) { selectedTab = 2 }
                SideBarItem("Resumen por Hospitales", Icons.Sharp.LocalHospital, selectedTab == 3) { selectedTab = 3 }
                SideBarItem("Informe de Consultas", Icons.AutoMirrored.Outlined.Help, selectedTab == 4) { selectedTab = 4 }
            }

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF121212))
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> DashboardContent(searchText, onSearchChange = { searchText = it })
                    1 -> PatientListContent()
                    2 -> DoctorListContent()
                    3 -> HospitalListContent()
//                    4 -> ConsultationReportContent()
                }
            }
        }
    }
}


