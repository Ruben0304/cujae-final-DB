package vistas.nav

import HospitalListContent
import PatientInfo
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
import auth.Auth
import dao.TurnoDAO
import kotlinx.coroutines.launch
import vistas.*
import vistas.login.LoginScreen
import vistas.login.RegisterScreen

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
        //Medico
        composable("turnos_medico") { TurnoTable() }
        composable("consulta_medico") { ConsultaTable() }



        //Admins
        composable("inicio") { if (Auth.rol == "medico") DashboardContentMedico() else DashboardContent()}
        composable("medicos") { DoctorListContent() }
        composable("hospitales") { HospitalListContent() }
        composable("buscar") { SearchScreen() }
        composable("info_pacientes") { PatientInfo() }
//        composable("pacientesHospital") { PacientesHospital() }
        composable("ia") { ConsultaIA() }
        composable("crear") { CreateFormScreen() }
        composable("register") { RegisterScreen() }
        composable("admins") { AccountsTable() }
        composable("departamentos") { DepartamentoTable(navController) }
        composable("unidadesH") { UnidadTable(
            onNavigateToPacientes = { unidadCodigo, departamentoCodigo ->
                navController.navigate("pacientes/$unidadCodigo/$departamentoCodigo")
            },
            onNavigateToTurnos = { unidadCodigo, departamentoCodigo ->
                navController.navigate("turnos/$unidadCodigo/$departamentoCodigo")
            },
            onNavigateToMedicos = { departamentoCodigo,unidadCodigo ->
                navController.navigate("medicos/$departamentoCodigo/$unidadCodigo")
            },
            onNavigateToInformes = { unidadCodigo,departamentoCodigo  ->
                navController.navigate("informes/$unidadCodigo/$departamentoCodigo")
            }
        ) }
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
                } ,
                        onNavigateToInformes = { unidadCodigo,departamentoCodigo ->
                    navController.navigate("informes/$unidadCodigo/$departamentoCodigo")
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
            )
        }
        composable(
            "consultas/{unidadCodigo}/{departamentoCodigo}/{turnoNumero}/{medico}",
            arguments = listOf(
                navArgument("unidadCodigo") { type = NavType.StringType },
                navArgument("departamentoCodigo") { type = NavType.StringType },
                navArgument("turnoNumero") { type = NavType.IntType },
                navArgument("medico") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val unidadCodigo = backStackEntry.arguments?.getString("unidadCodigo")
            val departamentoCodigo = backStackEntry.arguments?.getString("departamentoCodigo")
            val turnoNumero = backStackEntry.arguments?.getInt("turnoNumero")
            val medico = backStackEntry.arguments?.getString("medico")
            ConsultaTable(
                unidad = unidadCodigo.orEmpty(),
                departamento = departamentoCodigo.orEmpty(),
                numeroTurno = turnoNumero ?: 0,
                medico = medico.orEmpty()
            )
        }

        composable(
            "informes/{unidadCodigo}/{departamentoCodigo}",
            arguments = listOf(
                navArgument("unidadCodigo") { type = NavType.StringType },
                navArgument("departamentoCodigo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val unidadCodigo = backStackEntry.arguments?.getString("unidadCodigo")
            val departamentoCodigo = backStackEntry.arguments?.getString("departamentoCodigo")
            InformesTable(
                unidad = unidadCodigo.orEmpty(),
                departamento = departamentoCodigo.orEmpty()
            )
        }








//        composable("login") { LoginScreen() }
        // Agrega más composables aquí según sea necesario
    }

}