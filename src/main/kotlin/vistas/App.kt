package vistas

import DoctorListContent
import HospitalListContent
import PatientListContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun DashboardApp() {
    var selectedItem by remember { mutableStateOf("Inicio") }
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        // Body
        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            SideBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF121212))
                    .padding(16.dp)
            ) {
                when (selectedItem) {
                    "Inicio" -> DashboardContent()
                    "Pacientes" -> PatientListContent()
                    "Médicos" -> DoctorListContent()
                    "Hospitales" -> HospitalListContent()
//                    "Informes" -> ConsultationReportContent()
                    // Agrega más casos aquí para las nuevas secciones
                }
            }
        }
    }
}

