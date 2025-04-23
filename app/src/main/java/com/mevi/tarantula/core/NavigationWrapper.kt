package com.mevi.tarantula.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mevi.tarantula.iu.HomeScreen
import com.mevi.tarantula.iu.LoginScreen
import com.mevi.tarantula.iu.SplashScreen
import com.mevi.tarantula.iu.login.LoginViewModel
import com.mevi.tarantula.iu.pages.CategoryProductsPage
import com.mevi.tarantula.iu.pages.ProductDetailsPage

@Composable
fun NavigationWrapper(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    GlobalNavigation.navContoller = navController

    NavHost(navController = navController, startDestination = Splash) {
        composable<Splash> {
            SplashScreen(navController, loginViewModel)
        }
        composable<Login> {
            LoginScreen(modifier = modifier, loginViewModel) {
                navController.navigate(Home) {
                    popUpTo<Home> { inclusive = true }
                }
            }
        }
        composable<Home> {
            HomeScreen(modifier = modifier, navController)
        }

        composable("${CategoryProducts}/{categoryId}") {
            val categoryId = it.arguments?.getString("categoryId")
            CategoryProductsPage(modifier = modifier, navController, categoryId?: "")
        }

        composable("${ProductDetails}/{productId}") {
            val productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier = modifier, productId?: "")
        }
    }
}

object GlobalNavigation {
    lateinit var navContoller: NavHostController
}