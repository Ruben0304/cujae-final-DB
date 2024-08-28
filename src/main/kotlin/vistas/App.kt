package vistas

import HospitalListContent
import PatientListContent
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import vistas.login.DefaultPreview

import androidx.compose.animation.core.*
import androidx.compose.foundation.*

import androidx.compose.ui.draw.alpha

import androidx. compose.ui.window.*
import vistas.componentes.AnimatedFAB
import vistas.componentes.MacOSTitleBar
import vistas.componentes.SideBar
import vistas.login.LoginScreen


@Preview
@Composable
fun DashboardApp() {
    var selectedItem by remember { mutableStateOf("Inicio") }
    var fabExpanded by remember { mutableStateOf(false) }

    if (selectedItem == "Cerrar Sesión") {
        // Mostrar solo la pantalla de inicio de sesión (DefaultPreview)
        LoginScreen()
    } else {
        // Mostrar la estructura con la barra lateral y el contenido principal
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
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
                        "Buscar" -> SearchScreen()
                        "Crear" -> CreateProfileForm()
                        "Turnos" ->  TurnosTablePreview()
                        "Consultas" -> ConsultasTablePreview()
                        "Departamentos"-> DepartamentoTablePreview()
                        "Unidades"-> UnidadTablePreview()
                        // Agrega más casos aquí para las nuevas secciones
                    }
                }
            }

            // Overlay oscuro
            if (fabExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .alpha(
                            animateFloatAsState(
                                targetValue = if (fabExpanded) 1f else 0f,
                                animationSpec = tween(300)
                            ).value
                        )
                )
            }

            // FAB animado
            AnimatedFAB(
                expanded = fabExpanded,
                onExpandedChange = { fabExpanded = it },
                onItemSelected = { selectedItem = it }
            )
        }
    }
}


fun main() = application {
    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(900.dp, 700.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Proyecto final BD PostgreSQL",
        undecorated = true
    ) {
        Column {
            MacOSTitleBar(windowState)
            DashboardApp()
        }
    }
}


