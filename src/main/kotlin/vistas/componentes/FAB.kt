package vistas.componentes

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import vistas.colores.primaryColor


@Composable
fun AnimatedFAB(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (String) -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    val icon by remember(expanded) {
        derivedStateOf { if (expanded) Icons.Outlined.Add else Icons.Outlined.Apps }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 20.dp , bottom = 20.dp),
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    FloatingActionButton(
                        onClick = {  onItemSelected("Crear"); onExpandedChange(!expanded) },
                        containerColor = Color(0xff52ad6c),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Crear")
                    }
                    FloatingActionButton(
                        onClick = { onItemSelected("Buscar") ; onExpandedChange(!expanded) },
                        containerColor = Color(0xffa61631),
                        contentColor = Color.White

                    ) {
                        Icon(Icons.Outlined.Search, contentDescription = "Buscar")
                    }
                    FloatingActionButton(
                        onClick = { onExpandedChange(!expanded) },
                        containerColor = Color(0xffb79150),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Outlined.PictureAsPdf, contentDescription = "PDF")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            FloatingActionButton(
                onClick = { onExpandedChange(!expanded)},
                containerColor = primaryColor,
                modifier = Modifier.rotate(rotationAngle)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Expandir/Contraer",
                    tint = Color.White
                )
            }
        }
    }
}