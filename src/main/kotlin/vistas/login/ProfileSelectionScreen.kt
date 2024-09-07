package vistas.login

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dao.HospitalDAO
import global.Global

import kotlinx.coroutines.delay
import modelos.Hospital
import modelos.HospitalNombres
import vistas.DashboardApp
import vistas.componentes.ShowToast
import vistas.componentes.ToastHost
import vistas.componentes.ToastManager
import vistas.componentes.ToastType
import kotlin.random.Random

data class Profile(val name: String, val color: Color, val id: String)

@Composable
fun DefaultPreview() {
    var hospitals by remember { mutableStateOf<List<HospitalNombres>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedProfile by remember { mutableStateOf<Profile?>(null) }

    LaunchedEffect(Unit) {
        hospitals = HospitalDAO.getHospitals()
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(color = Color.Red)
    } else {
        val profiles = hospitals.map { hospital ->
            Profile(hospital.nombre, Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)),hospital.codigo)
        }



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
                    Global.selectedHospital = profile.id
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
        ToastManager.showToast("Sesión iniciada", ToastType.SUCCESS)
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
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center

    ) {
        items(profiles) { profile ->
            ProfileCard(profile){
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
            text = if (profile.name.length > 20) profile.name.take(20) + "..." else profile.name,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentSize(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )


    }
}