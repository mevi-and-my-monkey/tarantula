package com.mevi.tarantula.iu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.remoteconfig.remoteConfig
import com.mevi.tarantula.R
import com.mevi.tarantula.User
import com.mevi.tarantula.core.Home
import com.mevi.tarantula.core.Login
import com.mevi.tarantula.iu.login.LoginViewModel
import com.mevi.tarantula.ui.theme.TarantulaTheme
import com.mevi.tarantula.utils.Utilities
import kotlinx.coroutines.delay
import org.json.JSONArray

@Composable
fun SplashScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val adminEmails = remember { mutableStateOf<List<String>?>(null) }
    val isLoggedIn = Firebase.auth.currentUser != null
    val firstpage = if (isLoggedIn) Home else Login
    if (isLoggedIn) {
        Log.i("FIREBASE_USE", Firebase.auth.currentUser?.email.toString())
        User.userAdmin = Utilities.isAdmin(loginViewModel, Firebase.auth.currentUser?.email.toString())
        User.userInvited = false
    }
    LaunchedEffect(Unit) {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val jsonString = remoteConfig.getString("list_admin")
                val list = try {
                    val jsonArray = JSONArray(jsonString)
                    List(jsonArray.length()) { i -> jsonArray.getString(i) }
                } catch (e: Exception) {
                    Log.e("RemoteConfig", "Error al procesar JSON de list_admin", e)
                    emptyList()
                }
                loginViewModel.setAdminEmails(list)
                adminEmails.value = list
            } else {
                loginViewModel.setAdminEmails(emptyList())
                Log.e("RemoteConfig", "Error al obtener valores de Remote Config")
            }
        }
    }

    if (adminEmails.value != null) {
        Log.i("USERS_EMAIL", adminEmails.value.toString())
        LaunchedEffect(Unit) {
            delay(2000)
            navController.navigate(firstpage) {
                if (isLoggedIn) {
                    popUpTo<Home> { inclusive = true }
                } else {
                    popUpTo<Login> { inclusive = true }
                }
            }
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