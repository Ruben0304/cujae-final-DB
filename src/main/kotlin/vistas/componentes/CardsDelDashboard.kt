package vistas.componentes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedProgressCard(title: String, progress: Float, color: Color) {
    var startAnimation by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) progress else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }

    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(10.dp)
            .shadow(10.dp, shape = MaterialTheme.shapes.medium),
        backgroundColor = Color(0xFF1C1C1C)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(120.dp)) {
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
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PdfGeneratorCard() {
    val coroutineScope = rememberCoroutineScope()
    var isGenerating by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(10.dp, shape = MaterialTheme.shapes.medium),
        backgroundColor = Color(0xFF1E88E5)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.PictureAsPdf,
                contentDescription = "PDF",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Generar Informe PDF", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        isGenerating = true
                        delay(2000) // Simulando generación de PDF
                        isGenerating = false
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isGenerating) "Generando..." else "Generar PDF",
                    color = Color(0xFF1E88E5)
                )
            }
        }
    }
}

@Composable
fun AnimatedDataList(items: List<String>) {
    LazyColumn {
        items(items) { item ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                visible = true
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                    .animateContentSize()
                    .clip(MaterialTheme.shapes.medium),
                backgroundColor = if (visible) Color(0xFF424242) else Color.Transparent
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E88E5))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(item, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FloatingActionButtonWithMenu() {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().zIndex(1f)) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .absoluteOffset(x = (-16).dp, y = (-16).dp) // Ajusta estos valores según sea necesario
        ) {
            Column(horizontalAlignment = Alignment.End) {
                if (expanded) {
                    FloatingActionButton(
                        onClick = {  },
                        backgroundColor = Color(0xFFFFA000),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF")
                    }
//                    FloatingActionButton(
//                        onClick = { /* Acción 2 */ },
//                        backgroundColor = Color(0xFF66BB6A),
//                        modifier = Modifier.padding(bottom = 16.dp)
//                    ) {
//                        Icon(Icons.Default.Add, contentDescription = "Agregar")
//                    }
                }
                FloatingActionButton(
                    onClick = { expanded = !expanded },
                    backgroundColor = Color(0xFF1E88E5)

                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Expandir",
                        modifier = Modifier.rotate(animateFloatAsState(if (expanded) 45f else 0f).value)
                    )
                }

            }
        }
    }
}


@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        backgroundColor = Color(0xFF1C1C1C),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = Color.Gray)
                Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
        }
    }
}

