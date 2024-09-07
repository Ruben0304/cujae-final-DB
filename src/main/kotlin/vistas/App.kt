package vistas

import GlassmorphismDialogManager
import HospitalListContent
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

import androidx.compose.animation.core.*
import androidx.compose.foundation.*

import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush

import androidx. compose.ui.window.*
import vistas.componentes.AnimatedFAB
import vistas.componentes.MacOSTitleBar
import vistas.nav.SideBar
import vistas.login.LoginScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vistas.colores.backgroundGradient
import vistas.componentes.AceptCancelDialog
import vistas.componentes.AceptCancelDialogManager
import vistas.nav.EstablecerRutas

@Composable
fun DashboardApp() {
    val navController = rememberNavController()
    var fabExpanded by remember { mutableStateOf(false) }
    val isDialogOpen by remember { derivedStateOf { GlassmorphismDialogManager.isDialogOpen } }
    val isDialogOpen2 by remember { derivedStateOf { AceptCancelDialogManager.isDialogOpen } }

    // Animate the blur value
    val blurRadius by animateDpAsState(
        targetValue = if (isDialogOpen || isDialogOpen2) 15.dp else 0.dp,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xFF121212))
            .background(
                brush = Brush.verticalGradient(
                    colors = backgroundGradient
                )
            )
            .blur(blurRadius)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            SideBar(navController)

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(Color(0xFF121212))
                    .background(
                        Color.Transparent
                    )
                    .padding(16.dp)
            ) {
                // Configura el NavHost con las rutas
                EstablecerRutas(navController)
            }
        }
        if (fabExpanded || isDialogOpen || isDialogOpen2) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .alpha(
                        animateFloatAsState(
                            targetValue = if (fabExpanded || isDialogOpen || isDialogOpen2) 1f else 0f,
                            animationSpec = tween(300)
                        ).value
                    )
            )
        }

        // FAB animado
        AnimatedFAB(
            expanded = fabExpanded,
            onExpandedChange = { fabExpanded = it },
            onItemSelected = { route ->
                fabExpanded = false
                navController.navigate(route)
            }
        )
    }

    // Place the DialogHost outside the blurred content
    GlassmorphismDialogManager.DialogHost()
    AceptCancelDialogManager.DialogHost()
}

fun main() = application {
    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(950.dp, 700.dp)
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



