package vistas.login

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.Image
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import vistas.DashboardApp
import vistas.componentes.ShowToast
import vistas.componentes.ToastHost
import vistas.componentes.ToastManager
import vistas.componentes.ToastType

data class Profile(val name: String, val color: Color)

@Preview
@Composable
fun DefaultPreview() {
    val profiles = listOf(
        Profile("Calixto Garcia", Color(0xFF1E88E5)), // Azul
        Profile("Frank Pais", Color(0xFFE53935)), // Rojo
        Profile("Almejeiras", Color(0xFF37474F)), // Gris oscuro
        Profile("Pediatrico", Color(0xFF8E24AA)) // Morado
    )
    ProfileSelectionScreen(profiles)
    ToastManager.showToast("Sesión iniciada",ToastType.SUCCESS)
}


@Composable
fun ProfileSelectionScreen(profiles: List<Profile>) {
    var selectedProfile by remember { mutableStateOf<Profile?>(null) }


    // Perfil de selección
    AnimatedVisibility(
        visible = selectedProfile == null,
        enter = fadeIn(animationSpec = tween(700)) + slideInHorizontally(
            initialOffsetX = { it }, // Aparece desde la derecha
            animationSpec = tween(700)
        ),
        exit = fadeOut(animationSpec = tween(700)) + slideOutHorizontally(
            targetOffsetX = { -it }, // Desaparece hacia la izquierda
            animationSpec = tween(700)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Selecciona tu hospital",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Mostrar perfiles con animación
            AnimatedProfileRow(profiles) { profile ->
                selectedProfile = profile
            }

            // Dentro de la columna de selección de perfiles, después de AnimatedProfileRow
            Button(
                onClick = { /* Acción del botón */ },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xff5b5858)
                ),
                border = BorderStroke(.5.dp, Color(0xff5b5858)), // Borde casi blanco
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .padding(top = 30.dp)
                    .height(40.dp)
                    .width(230.dp)
            ) {
                Text(text = "Administrador general")
            }

        }
        ToastHost()
    }

    // Pantalla de login
    AnimatedVisibility(
        visible = selectedProfile != null,
        enter = fadeIn(animationSpec = tween(700)) + slideInHorizontally(
            initialOffsetX = { it }, // Aparece desde la izquierda
            animationSpec = tween(700)
        ),
        exit = fadeOut(animationSpec = tween(700)) + slideOutHorizontally(
            targetOffsetX = { -it }, // Desaparece hacia la derecha
            animationSpec = tween(700)
        )
    ) {
        DashboardApp()

    }
}








@Composable
fun AnimatedProfileRow(profiles: List<Profile>, onProfileSelected: (Profile) -> Unit) {
    var animatedProfiles by remember { mutableStateOf(emptyList<Profile>()) }

    LaunchedEffect(profiles) {
        profiles.forEachIndexed { index, profile ->
            delay((index * 40).toLong())
            animatedProfiles = animatedProfiles + profile
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 500))
    ) {
        animatedProfiles.forEach { profile ->
            ProfileCard(profile) {
                onProfileSelected(profile)
            }
        }
    }
}

@Composable
fun ProfileCard(profile: Profile, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .height(150.dp)
            .width(110.dp)
            .clickable { onClick() }
            .background(Color.Transparent)
            .animateContentSize(animationSpec = tween(durationMillis = 300)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(profile.color)
        ) {
            Image(
                painter = painterResource("Hospital.png"),
                contentDescription = "logo",
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center),

                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = profile.name,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentSize()
        )
    }
}