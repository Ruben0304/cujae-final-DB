package vistas.nav

import HospitalListContent
import PatientListContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vistas.DashboardContent
import vistas.DepartamentoTable
import vistas.DoctorListContent
import vistas.login.LoginScreen



@Composable
fun SideBar(navController: NavHostController) {
    var expandedMenu by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(Color(0xFF1E1E1E))
            .padding(vertical = 8.dp)
    ) {
        SideBarLogo()
        Divider(color = Color(0xFF3E3E3E), thickness = 1.dp, modifier = Modifier.padding(bottom = 20.dp))

        SideBarMenuItem("Inicio", Icons.Default.Home, navController) { navController.navigate("inicio") }

        SideBarMenuGroup("Gestión", Icons.Default.Business, expandedMenu) { expandedMenu = if (expandedMenu == "Gestión") "" else "Gestión" }
        if (expandedMenu == "Gestión") {
            SideBarSubMenuItem("Hospitales", navController) { navController.navigate("hospitales") }
            SideBarSubMenuItem("Departamentos", navController) { navController.navigate("departamentos") }
//            SideBarSubMenuItem("Unidades", navController) { navController.navigate("unidades") }
        }

        SideBarMenuGroup("Personal", Icons.Default.People, expandedMenu) { expandedMenu = if (expandedMenu == "Personal") "" else "Personal" }
        if (expandedMenu == "Personal") {
            SideBarSubMenuItem("Médicos", navController) { navController.navigate("medicos") }
            SideBarSubMenuItem("Pacientes", navController) { navController.navigate("pacientesHospital") }
        }

        // Nuevo elemento con burbuja de "IA"



//        SideBarMenuItem("Consultas", Icons.Default.EventNote, navController) { navController.navigate("consultas") }
//        SideBarMenuItem("Turnos", Icons.Default.Schedule, navController) { navController.navigate("turnos") }

//        SideBarMenuGroup("Informes", Icons.Default.Assessment, expandedMenu) { expandedMenu = if (expandedMenu == "Informes") "" else "Informes" }
//        if (expandedMenu == "Informes") {
//            SideBarSubMenuItem("Resúmenes", navController) { navController.navigate("resumenes") }
//            SideBarSubMenuItem("Listados", navController) { navController.navigate("listados") }
//        }
        SideBarMenuItemWithBubble("Consulta verbal", Icons.Default.AutoAwesome, navController) { navController.navigate("ia") }

        Spacer(modifier = Modifier.weight(1f))

        SideBarMenuItem("Cerrar Sesión", Icons.AutoMirrored.Filled.Logout, navController) { navController.navigate("login") }
    }
}

@Composable
fun SideBarLogo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(16, 78, 146), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalHospital,
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            "Hospital App",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SideBarMenuItem(text: String, icon: ImageVector, navController: NavHostController, onClick: () -> Unit) {
    val isSelected = navController.currentDestination?.route == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = when {
        isSelected -> Color(16, 78, 146)
        isHovered -> Color(0xFF2A2A2A)
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .hoverable(
                interactionSource = remember { MutableInteractionSource() },
                enabled = true
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun SideBarMenuGroup(text: String, icon: ImageVector, expandedMenu: String, onToggle: () -> Unit) {
    val isExpanded = expandedMenu == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = if (isHovered) Color(0xFF2A2A2A) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onToggle)
            .hoverable(
                interactionSource = remember { MutableInteractionSource() },
                enabled = true
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SideBarSubMenuItem(text: String, navController: NavHostController, onClick: () -> Unit) {
    val isSelected = navController.currentDestination?.route == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = when {
        isSelected -> Color(16, 78, 146)
        isHovered -> Color(0xFF2A2A2A)
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .hoverable(
                interactionSource = remember { MutableInteractionSource() },
                enabled = true
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color.White, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun SideBarMenuItemWithBubble(text: String, icon: ImageVector, navController: NavHostController, onClick: () -> Unit) {
    val isSelected = navController.currentDestination?.route == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = when {
        isSelected -> Color(16, 78, 146)
        isHovered -> Color(0xFF2A2A2A)
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .hoverable(
                interactionSource = remember { MutableInteractionSource() },
                enabled = true
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = Color.White, fontSize = 14.sp)

        // Burbuja de "IA"
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(24.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF64B5F6), Color(0xFF1E88E5))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("IA", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
