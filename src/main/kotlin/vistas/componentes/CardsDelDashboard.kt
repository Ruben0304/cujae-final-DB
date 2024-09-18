package vistas.componentes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vistas.colores.textColor

@Composable
fun AnimatedProgressCard(title: String, progress: Float, color: Color) {
    var startAnimation by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) progress else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }

    Surface(
        elevation = 4.dp,
//        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(400.dp)
            .height(300.dp)
            .padding(10.dp)
            .background(brush = Brush.radialGradient(listOf(
                color.copy(alpha = 0.25f),
                Color(216, 224, 231, 0x7c)
            ))
            ),
        color = Color.Transparent



    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = textColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(150.dp)) {
                    drawArc(
                        color = color.copy(alpha = 0.3f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 15f),
                        size = Size(size.width, size.height)
                    )
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        style = Stroke(width = 15f),
                        size = Size(size.width, size.height)
                    )
                }
                Text(
                    "${(animatedProgress * 100).toInt()}%",
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color) {
    Surface(
//        backgroundColor = Color.Transparent,
        elevation = 12.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
            )


    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = Color(110, 109, 109))
                Text(value, color = textColor, fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            }
        }
    }
}

