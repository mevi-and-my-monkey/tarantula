package com.mevi.tarantula.iu

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mevi.tarantula.User
import com.mevi.tarantula.core.Home
import com.mevi.tarantula.core.Login
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)

    ) {/*
        if (loginViewModel.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Fondo)
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
        } else {
            Body(loginViewModel, navigationToHome, Modifier.align(Alignment.Center))
            Footer(Modifier.align(Alignment.BottomCenter), loginViewModel, navigationToHome)
        }*/
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "es admin: ${User.userAdmin}", color = if(isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal)
            Button(onClick = {
                Firebase.auth.signOut()
                navController.navigate(Login){
                    popUpTo<Home> { inclusive = true }
                }
            }) {
                Text(text = "Cerrar sesion")
            }
        }
    }
}