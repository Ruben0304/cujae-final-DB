package vistas.nav

import HospitalListContent
import PatientListContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import vistas.*
import vistas.login.LoginScreen

@Composable
fun EstablecerRutas(navController: NavHostController) {
    // En la definición de las rutas dentro de DashboardApp:
    NavHost(
        navController = navController,
        startDestination = "inicio",
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 50))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 50))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 50))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 50))
        }

    ) {
        composable("inicio") { DashboardContent() }
        composable("medicos") { DoctorListContent() }
        composable("hospitales") { HospitalListContent() }
        composable("buscar") { SearchScreen() }
        composable("pacientesHospital") { PacientesHospital() }
        composable("ia") { ConsultaIA() }
        composable("crear") { CreateFormScreen() }
        composable("departamentos") { DepartamentoTable(navController) }
        composable(
            "unidades/{departamentoCodigo}",
            arguments = listOf(navArgument("departamentoCodigo") { type = NavType.StringType })
        ) { backStackEntry ->
            val departamentoCodigo = backStackEntry.arguments?.getString("departamentoCodigo")
            UnidadTable(
                departamentoCodigo = departamentoCodigo.orEmpty(),
                onNavigateToPacientes = { unidadCodigo, departamentoCodigo ->
                    navController.navigate("pacientes/$unidadCodigo/$departamentoCodigo")
                },
                onNavigateToTurnos = { unidadCodigo, departamentoCodigo ->
                    navController.navigate("turnos/$unidadCodigo/$departamentoCodigo")
                },
                onNavigateToMedicos = { departamentoCodigo,unidadCodigo ->
                    navController.navigate("medicos/$departamentoCodigo/$unidadCodigo")
                }
            )
        }
        composable(
            "medicos/{departamentoId}/{unidadCodigo}",
            arguments = listOf(
                navArgument("departamentoId") { type = NavType.StringType },
                navArgument("unidadCodigo") { type = NavType.StringType }
                )
        ) { backStackEntry ->
            val departamentoId = backStackEntry.arguments?.getString("departamentoId")
            val unidadCodigo = backStackEntry.arguments?.getString("unidadCodigo")
            DoctorListContent(
                departamentoId = departamentoId.orEmpty(),
                unidadCodigo = unidadCodigo.orEmpty()
            )
        }
        composable(
            "pacientes/{unidadCodigo}/{departamentoCodigo}",
            arguments = listOf(
                navArgument("unidadCodigo") { type = NavType.StringType },
                navArgument("departamentoCodigo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val unidadCodigo = backStackEntry.arguments?.getString("unidadCodigo")
            val departamentoCodigo = backStackEntry.arguments?.getString("departamentoCodigo")
            PatientListContent(
                unidadCodigo = unidadCodigo.orEmpty(),
                departamentoCodigo = departamentoCodigo.orEmpty()
            )
        }
        composable(
            "turnos/{unidadCodigo}/{departamentoCodigo}",
            arguments = listOf(
                navArgument("unidadCodigo") { type = NavType.StringType },
                navArgument("departamentoCodigo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val unidadCodigo = backStackEntry.arguments?.getString("unidadCodigo")
            val departamentoCodigo = backStackEntry.arguments?.getString("departamentoCodigo")
            TurnoTable(
                unidad = unidadCodigo.orEmpty(),
                departamento = departamentoCodigo.orEmpty(),
                onNavigateToConsultas = { unidad, departamento, turnoNumero ->
                    navController.navigate("consultas/$unidad/$departamento/$turnoNumero")
                }
            )
        }
        composable(
            "consultas/{unidadCodigo}/{departamentoCodigo}/{turnoNumero}",
            arguments = listOf(
                navArgument("unidadCodigo") { type = NavType.StringType },
                navArgument("departamentoCodigo") { type = NavType.StringType },
                navArgument("turnoNumero") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val unidadCodigo = backStackEntry.arguments?.getString("unidadCodigo")
            val departamentoCodigo = backStackEntry.arguments?.getString("departamentoCodigo")
            val turnoNumero = backStackEntry.arguments?.getInt("turnoNumero")
            ConsultaTable(
                unidad = unidadCodigo.orEmpty(),
                departamento = departamentoCodigo.orEmpty(),
                numeroTurno = turnoNumero ?: 0
            )
        }
        composable("login") { LoginScreen() }
        // Agrega más composables aquí según sea necesario
    }

}