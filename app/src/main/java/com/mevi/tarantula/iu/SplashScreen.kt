package com.mevi.tarantula.iu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mevi.tarantula.R
import com.mevi.tarantula.core.Login
import com.mevi.tarantula.ui.theme.TarantulaTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true) {
        delay(5000)
        navController.navigate(Login) {
            popUpTo<Login> { inclusive = true }
        }
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.logo_splash),
            contentDescription = "Logo Splash",
            modifier = Modifier.size(150.dp, 150.dp)
        )
    }
}

@Preview()
@Composable
fun ScreenSplashPreview() {
    TarantulaTheme {
        Splash()
    }
}