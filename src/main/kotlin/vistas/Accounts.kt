package vistas

import PatientListContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import auth.Auth
import dao.AccountDAO
import dao.DepartamentoDAO
import global.Global
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import modelos.Departamento
import modelos.UserConMetadata
import vistas.colores.textColor
import vistas.componentes.ToastManager
import vistas.componentes.ToastType
import vistas.nav.NavManager


@Composable
fun AccountsTable() {
    val surfaceColor = Color(0xffffffff)
    val headerColor = Color(0xe4000000)
    val dividerColor = Color(0xba949494)
    val linkColor = Color(0xff5073ec)
    var cuentas by remember { mutableStateOf(listOf<UserConMetadata>()) }
    var isLoading by remember { mutableStateOf(true) }

    val corutineScope = rememberCoroutineScope()


    LaunchedEffect(key1 = true) {
        if (Auth.rol == "admin_general")
            cuentas = AccountDAO.obtenerUsers("admin_hospital")
        else
            cuentas = AccountDAO.obtenerUsers("medico", Auth.hospital)

        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(16, 78, 146))
            }
        } else {
            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(animationSpec = tween(durationMillis = 100))
            ) {
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(headerColor)
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            listOf("Email", "Nombre", "Hospital", "").forEach { header ->
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = header,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                    )
                                }
                            }
                        }

                        // Rows
                        LazyColumn {
                            itemsIndexed(cuentas) { index, c ->
                                val animatedProgress =
                                    remember { androidx.compose.animation.core.Animatable(initialValue = 0f) }

                                LaunchedEffect(key1 = Unit) {
                                    animatedProgress.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(
                                            durationMillis = 250,
                                            easing = EaseOutQuad,
                                            delayMillis = index * 60
                                        )
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .alpha(animatedProgress.value)
                                        .offset(y = (20 * (1f - animatedProgress.value)).dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 16.dp),

                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) { c.email?.let { Text(it, color = textColor) } }
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) { Text(c.nombre, color = textColor) }
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) { c.hospital?.let { Text(it, color = textColor) } }


                                        // Ver link
                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Eliminar cuenta",
                                                color = Color(154, 46, 67),
                                                modifier = Modifier
                                                    .clickable {
                                                        try {
                                                            corutineScope.launch {
                                                                Auth.deleteUser(c.id)

                                                                NavManager.refresh()
                                                                ToastManager.showToast(
                                                                    "Eliminado correctamente",
                                                                    ToastType.SUCCESS
                                                                )
                                                            }
                                                        } catch (e: Exception) {
                                                            ToastManager.showToast(
                                                                "Error: ${e.message}",
                                                                ToastType.ERROR
                                                            )
                                                        }
//
                                                    }
                                                    .padding(4.dp)
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
