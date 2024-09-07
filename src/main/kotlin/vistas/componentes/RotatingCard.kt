package vistas.componentes

import DialogButton
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.MoreHoriz


import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import kotlin.math.absoluteValue


@Composable
fun RotatingCard(
    frontGradient: List<Color>,
    labelText: String,
    avatar: Painter,
    titleText: String,
    subtitleText: String,
    infoItems: List<InfoItem>,
    onMoreClick: () -> Unit
) {
    var isRotated by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isRotated) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .size(190.dp, 254.dp)
            .graphicsLayer { rotationY = rotation }
            .clickable { isRotated = !isRotated }
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = .5.dp,
                color = Color.DarkGray,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        if (rotation.absoluteValue < 90f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF151515))
                    .clip(RoundedCornerShape(5.dp))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FrontContent(frontGradient, labelText, avatar, titleText, subtitleText,onMoreClick)
                }
            }
        } else {
            BackSide(infoItems, frontGradient)
        }
    }
}

@Composable
fun FrontContent(
    gradient: List<Color>,
    labelText: String,
    avatar: Painter,
    titleText: String,
    subtitleText: String,
    onMoreClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = labelText,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(colors = gradient),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(4.dp)
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = avatar,
                    contentDescription = "Main Icon",
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
//                        brush = Brush.linearGradient(colors = gradient),
                        color = Color.Transparent,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = titleText,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = subtitleText,
                    color = Color(0xFFFFFFFF).copy(alpha = 0.55f),
                    fontSize = 8.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp, top = 8.dp)
                .clickable { onMoreClick() },
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreHoriz,
                contentDescription = "Acciones",
                tint = Color(255,255,255),
                modifier = Modifier.size(28.dp)

            )
        }
    }
}

data class InfoItem(
    val icon: ImageVector,
    val label: String,
    val value: String
)

@Composable
fun BackSide(infoItems: List<InfoItem>, gradient: List<Color> ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background( brush = Brush.linearGradient(
                colors = gradient
            ))
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(16.dp))
            .padding(20.dp)
            .graphicsLayer(scaleX = -1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            infoItems.forEach { item ->
                InfoItemLoad(icon = item.icon, label = item.label, value = item.value)
            }
        }
    }
}

@Composable
fun InfoItemLoad(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = Color(0xFF999999)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color.White
            )
        }
    }
}
