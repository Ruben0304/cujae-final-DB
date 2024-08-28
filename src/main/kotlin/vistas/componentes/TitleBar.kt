package vistas.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import kotlin.system.exitProcess


@Composable
fun MacOSTitleBar(windowState: WindowState) {
    val density = LocalDensity.current
    var isDragging by remember { mutableStateOf(false) }
    var startPosition by remember { mutableStateOf(WindowPosition.Absolute(0.dp, 0.dp)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color(0xFF2D2D2D))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                        startPosition = windowState.position as WindowPosition.Absolute
                    },
                    onDragEnd = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (isDragging) {
                            val currentPosition = windowState.position
                            windowState.position = WindowPosition.Absolute(
                                x = currentPosition.x + dragAmount.x.toDp(),
                                y = currentPosition.y + dragAmount.y.toDp()
                            )
                        }
                    }
                )
            },
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MacOSWindowButton(
            color = Color(0xFFFFBD2E),
            onClick = { windowState.isMinimized = true }
        )
        MacOSWindowButton(
            color = Color(0xFF28C940),
            onClick = {
                windowState.placement = if (windowState.placement == WindowPlacement.Maximized)
                    WindowPlacement.Floating
                else
                    WindowPlacement.Maximized
            }
        )
        MacOSWindowButton(
            color = Color(0xFFFF5F57),
            onClick = { exitProcess(0) }
        )
        Spacer(Modifier.width(16.dp))
    }
}

@Composable
fun MacOSWindowButton(color: Color, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(12.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = if (isHovered) 1f else 0.8f))
            .hoverable(interactionSource)
            .clickable(onClick = onClick)
    ) {
        if (isHovered) {
            when (color) {
                Color(0xFFFF5F57) -> Text("×", color = Color.Black, fontSize = 10.sp, modifier = Modifier.align(
                    Alignment.Center))
                Color(0xFFFFBD2E) -> Text("−", color = Color.Black, fontSize = 10.sp, modifier = Modifier.align(
                    Alignment.Center))
                Color(0xFF28C940) -> Text("+", color = Color.Black, fontSize = 10.sp, modifier = Modifier.align(
                    Alignment.Center))
            }
        }
    }
}