package com.mevi.tarantula

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.mevi.tarantula.core.NavigationWrapper
import com.mevi.tarantula.iu.login.LoginViewModel
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.TarantulaTheme

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TarantulaTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(Primario)) { innerPadding ->
                    NavigationWrapper(loginViewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
