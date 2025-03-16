package com.mevi.tarantula.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mevi.tarantula.iu.LoginScreen
import com.mevi.tarantula.iu.SplashScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Splash) {
        composable<Splash> {
            SplashScreen { navController.navigate(Login) }
        }
        composable<Login> {
            LoginScreen { navController.navigate(Home)}
        }
    }
}