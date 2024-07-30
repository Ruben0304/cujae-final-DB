package vistas

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SideBar(selectedItem: String, onItemSelected: (String) -> Unit) {
    var expandedMenu by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(Color(0xFF1E1E1E))
            .padding(vertical = 8.dp)

    ) {
        SideBarLogo()
        Divider(color = Color(0xFF3E3E3E), thickness = 1.dp,modifier = Modifier.padding(bottom = 20.dp))

        SideBarMenuItem("Inicio", Icons.Default.Home, selectedItem, onItemSelected)

        SideBarMenuGroup("Gestión", Icons.Default.Business, expandedMenu) { expandedMenu = if (expandedMenu == "Gestión") "" else "Gestión" }
        if (expandedMenu == "Gestión") {
            SideBarSubMenuItem("Hospitales", selectedItem, onItemSelected)
            SideBarSubMenuItem("Departamentos", selectedItem, onItemSelected)
            SideBarSubMenuItem("Unidades", selectedItem, onItemSelected)
        }

        SideBarMenuGroup("Personal", Icons.Default.People, expandedMenu) { expandedMenu = if (expandedMenu == "Personal") "" else "Personal" }
        if (expandedMenu == "Personal") {
            SideBarSubMenuItem("Médicos", selectedItem, onItemSelected)
            SideBarSubMenuItem("Pacientes", selectedItem, onItemSelected)
        }

        SideBarMenuItem("Consultas", Icons.Default.EventNote, selectedItem, onItemSelected)
        SideBarMenuItem("Turnos", Icons.Default.Schedule, selectedItem, onItemSelected)

        SideBarMenuGroup("Informes", Icons.Default.Assessment, expandedMenu) { expandedMenu = if (expandedMenu == "Informes") "" else "Informes" }
        if (expandedMenu == "Informes") {
            SideBarSubMenuItem("Resúmenes", selectedItem, onItemSelected)
            SideBarSubMenuItem("Listados", selectedItem, onItemSelected)
        }

        Spacer(modifier = Modifier.weight(1f))

        SideBarMenuItem("Configuración", Icons.Default.Settings, selectedItem, onItemSelected)
        SideBarMenuItem("Búsqueda", Icons.Default.Search, selectedItem, onItemSelected)
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
fun SideBarMenuItem(text: String, icon: ImageVector, selectedItem: String, onItemSelected: (String) -> Unit) {
    val isSelected = selectedItem == text
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
            .clickable { onItemSelected(text) }
            .hoverable(
                interactionSource = remember { MutableInteractionSource() },

                enabled = true,

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

                enabled = true,

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
fun SideBarSubMenuItem(text: String, selectedItem: String, onItemSelected: (String) -> Unit) {
    val isSelected = selectedItem == text
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
            .clickable { onItemSelected(text) }
            .hoverable(
                interactionSource = remember { MutableInteractionSource() },

                enabled = true,

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