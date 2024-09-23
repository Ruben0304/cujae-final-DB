package vistas

import GlassmorphismDialogManager
import HospitalListContent
import androidx.compose.animation.animateColor
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface

import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.window.*
import androidx.navigation.NavHostController
import vistas.nav.SideBar
import vistas.login.LoginScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import auth.Auth
import kotlinx.coroutines.launch
import vistas.componentes.*
import vistas.login.DefaultPreview
import vistas.nav.EstablecerRutas
import vistas.nav.NavManager

@Composable
fun DashboardApp() {
    val navController = rememberNavController()

    val isDialogOpen by remember { derivedStateOf { GlassmorphismDialogManager.isDialogOpen } }
    val isDialogOpen2 by remember { derivedStateOf { AceptCancelDialogManager.isDialogOpen } }
    val isDialogOpen3 by remember { derivedStateOf { EditDialogManager.isDialogOpen } }
    var isLoggedIn by remember { mutableStateOf(Auth.isSessionActive()) }
    var isProfileSelectedScreenNeeded by remember { mutableStateOf(false) }
    val corrutineScope = rememberCoroutineScope()


    val colorPalette = listOf(
        Color(0xFFFFD6D6),  // Soft pink
        Color(0xFFD6FFFE),  // Soft cyan
        Color(0xFFE6D6FF),  // Soft lavender
        Color(0xFFFFF7D6),  // Soft yellow
        Color(0xFFD6FFE1)   // Soft mint
    )

    val infiniteTransition = rememberInfiniteTransition()

    val colors = colorPalette.mapIndexed { index, color ->
        val nextColor = colorPalette[(index + 1) % colorPalette.size]
        infiniteTransition.animateColor(
            initialValue = color,
            targetValue = nextColor,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing)
        )
    )

    val blurRadius by animateDpAsState(
        targetValue = if (isDialogOpen || isDialogOpen2 || isDialogOpen3) 15.dp else 0.dp,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )



    LaunchedEffect(isLoggedIn, isProfileSelectedScreenNeeded) {
//        Auth.verificarSesionAlIniciar()
        if (isLoggedIn) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        } else if (isProfileSelectedScreenNeeded) {
            navController.navigate("profileScreen") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("main") { inclusive = true }
            }
        }
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.sweepGradient(
                    colors = colors.map { it.value },
                    center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f)
                )
            )
            .blur(blurRadius)


    ) {

        NavHost(navController, startDestination = "login") {
//        NavHost(navController, startDestination = if (isLoggedIn) "main" else "login") {
            composable("login") {
                LoginScreen {
                    if (Auth.rol != "admin_general")
                        isLoggedIn = true
                    else
                        isProfileSelectedScreenNeeded = true

                }
            }
            composable("main") {
                AppContent(
                    onLogout = {
                        corrutineScope.launch {
                            Auth.logout()
                            isLoggedIn = false
                        }
                    }
                )
            }
            composable("profileScreen") {
                DefaultPreview(
                    onSelect = { profile ->
                        Auth.hospital = profile
                        isProfileSelectedScreenNeeded = false
                        isLoggedIn = true
                    })


            }


        }
    }
//    ToastHost()
    // Place the DialogHost outside the blurred content
    GlassmorphismDialogManager.DialogHost()
    AceptCancelDialogManager.DialogHost()
    EditDialogManager.DialogHost()
    CausaDialogManager.DialogHost()

}


@Composable
fun AppContent(onLogout: () -> Unit) {
    val innerNavController = rememberNavController()
    var fabExpanded by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxSize()) {


        // Sidebar
        SideBar(innerNavController, onLogout)
        NavManager.navController = innerNavController

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Transparent
                )
                .padding(16.dp)
        ) {
            // Configura el NavHost con las rutas
            EstablecerRutas(innerNavController)
        }
    }
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
    if(Auth.rol != "medico")
    AnimatedFAB(
        expanded = fabExpanded,
        onExpandedChange = { fabExpanded = it },
        onItemSelected = { route ->
            fabExpanded = false
            innerNavController.navigate(route)
        }
    )
}


fun main() = application {
    val windowState = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(1000.dp, 700.dp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Proyecto final BD PostgreSQL",
        undecorated = true,
        transparent = true

    ) {
        Column(Modifier.shadow(10.dp, RoundedCornerShape(16.dp))) {
            MacOSTitleBar(windowState)
            DashboardApp()
            ToastHost()
        }
    }
}



