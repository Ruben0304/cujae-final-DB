package vistas

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.delay
import vistas.componentes.SelectInputField
import vistas.componentes.SubmitButton
import vistas.componentes.TextAreaField
import vistas.componentes.TextInputField
import java.io.File

@Composable
fun CreateProfileForm() {
    var previewUrl by remember { mutableStateOf<String?>(null) }
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(.9f),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(12.dp)

        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Crear Nuevo Perfil",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier.padding(bottom = 24.dp)
                )



                Spacer(modifier = Modifier.height(50.dp)) // Espacio vertical

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        TextInputField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nombre completo"
                        )
                        Spacer(modifier = Modifier.height(23.dp)) // Espacio vertical
                        TextInputField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Nombre de usuario"
                        )
                        Spacer(modifier = Modifier.height(23.dp)) // Espacio vertical
                        TextInputField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = "Teléfono",
                            keyboardType = KeyboardType.Phone
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        SelectInputField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = "Género",
                            options = listOf("Masculino", "Femenino", "Otro", "Prefiero no decir")
                        )
                        Spacer(modifier = Modifier.height(23.dp)) // Espacio vertical
                        SelectInputField(
                            value = country,
                            onValueChange = { country = it },
                            label = "País",
                            options = listOf("España", "México", "Argentina", "Colombia", "Perú")
                        )
                        Spacer(modifier = Modifier.height(23.dp)) // Espacio vertical
                        TextAreaField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = "Biografía"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp)) // Espacio vertical
                // Submit Button
                SubmitButton()
            }
        }
    }
}

fun main() = singleWindowApplication {
    CreateProfileForm()
}
