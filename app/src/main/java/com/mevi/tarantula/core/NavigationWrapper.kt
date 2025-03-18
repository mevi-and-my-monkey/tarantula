package com.mevi.tarantula.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mevi.tarantula.iu.HomeScreen
import com.mevi.tarantula.iu.LoginScreen
import com.mevi.tarantula.iu.SplashScreen
import com.mevi.tarantula.iu.login.LoginViewModel

@Composable
fun NavigationWrapper(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Splash) {
        composable<Splash> {
            SplashScreen(navController, loginViewModel)
        }
        composable<Login> {
            LoginScreen(
                modifier = modifier,
                loginViewModel,
            ) { navController.navigate(Home) }
        }
        composable<Home> {
            HomeScreen(navController)
        }
    }
}