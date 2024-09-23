package vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.LocalHospital
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
import dao.CantidadesDAO.obtenerConteoPacientesPorEstado
import dao.DoctorDAO
import dao.PatientDAO
import global.Global
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import supabase.Supabase
import vistas.colores.SoftBlue
import vistas.colores.SoftGreen
import vistas.colores.SoftRed
import vistas.colores.textColor
import vistas.componentes.*


@Composable
fun DashboardContent() {
    val coroutineScope = rememberCoroutineScope()

    var noAtendidos by remember { mutableStateOf(0) }
    var atendidos by remember { mutableStateOf(0) }
    var fallecidos by remember { mutableStateOf(0) }
    var pendientes by remember { mutableStateOf(0) }
    var dadosDeAlta by remember { mutableStateOf(0) }
    var totalPacientes by remember { mutableStateOf(0) }
    var cantidadDepartamentos by remember { mutableStateOf(0) }
    var cantidadUnidades by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {

//Auth.logout()
//        Auth.login("a@a.com", "a")


//        Auth.logout()
//        Auth.cambiar_rol("fc568479-5527-44e0-89ce-06e162575b68", "service_role")

//        Auth.login("a@a.com", "a")
//        Supabase.coneccion.auth.importAuthToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndnZmRtZ3Nic3FmbGNndGVjZ2x3Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyNDk2NjU3NSwiZXhwIjoyMDQwNTQyNTc1fQ.44s9ndcvYmbaFE1iJWwvrFjdcK8Z0PjEihGVS-WP6Nk")

        println(Auth.rol)
        if (Auth.hospital != "") {
            try {
                val conteoPacientes = obtenerConteoPacientesPorEstado(Auth.hospital)
                noAtendidos = conteoPacientes.no_atendidos
                atendidos = conteoPacientes.atendidos
                fallecidos = conteoPacientes.fallecidos
                pendientes = conteoPacientes.pendientes
                dadosDeAlta = conteoPacientes.dados_de_alta
                cantidadDepartamentos = conteoPacientes.cantidad_departamentos
                cantidadUnidades = conteoPacientes.cantidad_unidades
                totalPacientes = noAtendidos + atendidos + pendientes

            } catch (e: Exception) {
                ToastManager.showToast(e.message.toString(), ToastType.INFO)
            }

        }
        isLoading = false
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Transparent)
    ) {
        // Agregar el componente de bienvenida en la parte superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Espacio vacío a la izquierda para mantener el avatar a la derecha
            Spacer(modifier = Modifier.weight(1f))

            // Componente de bienvenida
            WelcomeAvatar()
        }
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(color = Color.Blue)
            }
        } else {
            // Agregar dropdown filtrador y estilo futurista
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Componente de dropdown aquí
            }

            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                StatCard(
                    "Unidades",
                    cantidadUnidades.toString(),
                    Icons.Outlined.CheckCircle,
                    SoftBlue
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatCard(
                    "Departamentos",
                    cantidadDepartamentos.toString(),
                    Icons.Outlined.ErrorOutline,
                    SoftRed
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatCard(
                    "Pacientes no atendidos",
                    noAtendidos.toString(),
                    Icons.Outlined.LocalHospital,
                    SoftGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nuevos componentes con estilo Glassmorphism
            if (noAtendidos != 0) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    AnimatedProgressCard(
                        "Atendidos",
                        atendidos.toFloat() / totalPacientes.toFloat(),
                        Color(88, 185, 106)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    AnimatedProgressCard(
                        "No atendidos",
                        noAtendidos.toFloat() / totalPacientes.toFloat(),
                        Color(187, 59, 59)
                    )
                }
            }
        }
    }
}

