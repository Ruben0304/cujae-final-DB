package vistas.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.Auth


@Composable
fun WelcomeAvatar() {
    var user by remember { mutableStateOf("") }
    LaunchedEffect(true) {
        if (Auth.session_actual() != null)
            user = Auth.session_actual()?.user?.email.toString()
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {


        Text(
            text = user,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(114, 113, 113),
            modifier = Modifier.padding(end = 8.dp)

        )
        Image(
            painter = painterResource("profile.jpg"),
            contentDescription = "Avatar del usuario",
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .border(width = .7.dp, color = Color.LightGray, CircleShape)
        )
    }
}