package vistas.nav

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

object NavManager {
  lateinit var navController : NavController


  fun refresh(){
    navController.navigate(navController.currentDestination?.route.toString())
  }
}