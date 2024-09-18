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


import androidx.compose.foundation.layout.*

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip

import androidx.compose.ui.draw.shadow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*

import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip

import androidx.compose.ui.unit.dp
import auth.Auth
import kotlinx.coroutines.launch
import vistas.colores.*


val menuItemTextSize = 14.sp
val subMenuItemTextSize = 12.sp
val logoTextSize = 20.sp


@Composable
fun SideBar(navController: NavHostController, onLogout: () -> Unit) {
    var expandedMenu by remember { mutableStateOf("") }

    Surface(
        elevation = 6.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .width(220.dp)
            .fillMaxHeight()
            .background(Color.Transparent)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(
//                    Brush.linearGradient(
//                        colors = listOf(
//                            Color(0x3c24272c),
//                            Color(0x74475d8d)
//                        )),
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                ),

            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 12.dp)
            ) {
                SideBarLogo()
                Divider(color = Color(0xFF3E3E3E), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

                SideBarMenuItem("Inicio", Icons.Default.Home, navController) { navController.navigate("inicio") }

                when(Auth.rol){
                    "admin_general","admin_hospital","service_role" -> {
                        SideBarMenuGroup("Gestión", Icons.Default.Business, expandedMenu) {
                            expandedMenu = if (expandedMenu == "Gestión") "" else "Gestión"
                        }
                        if (expandedMenu == "Gestión") {

                                SideBarSubMenuItem("Hospitales", navController) { navController.navigate("hospitales") }
                                SideBarSubMenuItem("Departamentos", navController) { navController.navigate("departamentos") }
                                SideBarSubMenuItem("Unidades", navController) { navController.navigate("unidadesH") }
                        }

                        SideBarMenuGroup("Personal", Icons.Default.People, expandedMenu) {
                            expandedMenu = if (expandedMenu == "Personal") "" else "Personal"
                        }
                        if (expandedMenu == "Personal") {
                            SideBarSubMenuItem("Médicos", navController) { navController.navigate("medicos") }
                            SideBarSubMenuItem("Pacientes", navController) { navController.navigate("pacientesHospital") }
                        }

                        SideBarMenuItemWithBubble(
                            "Consulta verbal",
                            Icons.Default.AutoAwesome,
                            navController
                        ) { navController.navigate("ia") }


                            SideBarMenuItem(
                                "Cuentas",
                                Icons.Default.ManageAccounts,
                                navController
                            ) { navController.navigate("admins") }

                        Spacer(modifier = Modifier.weight(1f))

                        SideBarMenuItem(
                            "Crear cuenta",
                            Icons.Default.AccountCircle,
                            navController
                        ) { navController.navigate("register") }
                        SideBarMenuItem(
                            "Cerrar Sesión",
                            Icons.AutoMirrored.Filled.Logout,
                            navController
                        ) { onLogout() }
                    }




                    //medico

                    "medico" -> {
                        SideBarMenuItem("Turnos", Icons.Filled.CalendarToday, navController) { navController.navigate("turnos_medico") }
                        SideBarMenuItem("Consultas", Icons.Filled.MedicalServices, navController) { navController.navigate("consulta_medico") }
                        Spacer(modifier = Modifier.weight(1f))
                        SideBarMenuItem(
                            "Cerrar Sesión",
                            Icons.AutoMirrored.Filled.Logout,
                            navController
                        ) { onLogout() }
                    }






                }







            }
        }
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
        Icon(
            imageVector = Icons.Default.MedicalServices,
            contentDescription = "Logo",
            tint = primaryColor,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            "MediCare",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = logoTextSize
        )
    }
}

@Composable
fun SideBarMenuItem(text: String, icon: ImageVector, navController: NavHostController, onClick: () -> Unit) {
    val isSelected = navController.currentDestination?.route == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = when {
        isSelected -> selectedBackgroundColor
        isHovered -> hoverColor
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .hoverable(interactionSource = remember { MutableInteractionSource() })
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isSelected) selectedTextColor else textColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = if (isSelected) selectedTextColor else textColor,
            fontSize = menuItemTextSize,
            fontWeight = if (isSelected) selectedItemFontWeight else menuItemFontWeight
        )
    }
}

@Composable
fun SideBarMenuGroup(text: String, icon: ImageVector, expandedMenu: String, onToggle: () -> Unit) {
    val isExpanded = expandedMenu == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = if (isHovered) hoverColor else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onToggle)
            .hoverable(interactionSource = remember { MutableInteractionSource() })
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = menuItemTextSize,
            fontWeight = menuItemFontWeight
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SideBarSubMenuItem(text: String, navController: NavHostController, onClick: () -> Unit) {
    val isSelected = navController.currentDestination?.route == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = when {
        isSelected -> selectedBackgroundColor
        isHovered -> hoverColor
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 56.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .hoverable(interactionSource = remember { MutableInteractionSource() })
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = if (isSelected) selectedTextColor else textColor,
            fontSize = subMenuItemTextSize,
            fontWeight = if (isSelected) selectedItemFontWeight else subMenuItemFontWeight
        )
    }
}

@Composable
fun SideBarMenuItemWithBubble(text: String, icon: ImageVector, navController: NavHostController, onClick: () -> Unit) {
    val isSelected = navController.currentDestination?.route == text
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor = when {
        isSelected -> selectedBackgroundColor
        isHovered -> hoverColor
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .hoverable(interactionSource = remember { MutableInteractionSource() })
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isSelected) selectedTextColor else textColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = if (isSelected) selectedTextColor else textColor,
            fontSize = menuItemTextSize,
            fontWeight = if (isSelected) selectedItemFontWeight else menuItemFontWeight
        )

        Spacer(modifier = Modifier.weight(1f))
//        Box(
//            modifier = Modifier
//                .size(24.dp)
//                .background(
//                    brush = Brush.linearGradient(colors = gradientColors),
//                    shape = CircleShape
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("IA", color = Color.White, fontSize = 6.sp, fontWeight = FontWeight.Bold)
//        }
    }
}