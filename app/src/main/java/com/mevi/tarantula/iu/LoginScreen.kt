package com.mevi.tarantula.iu

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mevi.tarantula.R
import com.mevi.tarantula.iu.login.LoginViewModel
import com.mevi.tarantula.ui.theme.AppShapes
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoSecundario

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(),
    navigationToHome: () -> Unit
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Primario)
            .padding(8.dp)
    ) {
        if (!loginViewModel.isLoading) {
            Box(
                modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        } else {
            Body(loginViewModel, navigationToHome, Modifier.align(Alignment.Center))
            Footer(Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun Body(loginViewModel: LoginViewModel, navigationToHome: () -> Unit, modifier: Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Iniciar sesión",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Email(loginViewModel.email, modifier) {
            loginViewModel.onLoginChanged(email = it, password = loginViewModel.password)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Password(loginViewModel.password, modifier) {
            loginViewModel.onLoginChanged(email = loginViewModel.email, password = it)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Has olvidado tu contraseña",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            modifier = Modifier.clickable { /* Acción de recuperación */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LoginButton(loginViewModel.isLoginEnable, loginViewModel, navigationToHome)


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { },
            shape = AppShapes.medium,
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextoSecundario
            ),
            border = BorderStroke(2.dp, Primario)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_google),
                contentDescription = "Google",
                tint = Primario
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continua con Google", color = TextoSecundario)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { /* Acción de invitado */ },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextoSecundario,
            ),
            border = BorderStroke(2.dp, Primario)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_guest),
                contentDescription = "Invitado",
                tint = Primario
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continua como invitado", color = TextoSecundario)
        }
    }
}

@Composable
fun Footer(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        SignUp(modifier)
    }
}

@Composable
fun SignUp(modifier: Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("¿No tienes una cuenta?", color = TextoSecundario, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Crear cuenta",
            color = TextoPrincipal,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { /* Acción de registro */ }
        )
        Spacer(modifier = modifier.height(8.dp))
    }
    Spacer(modifier = modifier.height(8.dp))
    Text(
        "Tarantula by Mevi®",
        color = TextoSecundario,
        fontSize = 12.sp,
        textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth()
    )
    Spacer(modifier = modifier.height(8.dp))
}

@Composable
fun Email(email: String, modifier: Modifier, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = {
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.ic_email),
                contentDescription = "Email",
                tint = Primario
            )
        },
        placeholder = { Text("Correo electrónico") },
        modifier = modifier.fillMaxWidth(0.8f),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = TextoSecundario,
            focusedTextColor = TextoPrincipal,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Primario,
            focusedBorderColor = Primario,
            unfocusedBorderColor = Secundario
        )
    )
}

@Composable
fun Password(password: String, modifier: Modifier, onTextChanged: (String) -> Unit) {
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        placeholder = { Text("Contraseña") },
        modifier = modifier.fillMaxWidth(0.8f),
        trailingIcon = {
            val imagen = if (passwordVisibility) {
                Icons.Filled.Clear
            } else {
                Icons.Filled.Check
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = imagen, contentDescription = "show password", tint = Primario)
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = TextoSecundario,
            focusedTextColor = TextoPrincipal,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Primario,
            focusedBorderColor = Primario,
            unfocusedBorderColor = Secundario
        )
    )
}

@Composable
fun LoginButton(
    loginEnable: Boolean,
    loginViewModel: LoginViewModel,
    navigationToHome: () -> Unit
) {
    Button(
        onClick = {
            loginViewModel.signUp("Ale") { success, resultMessage ->
                if (success) {
                    Log.i("LOG", "$resultMessage")
                    navigationToHome()
                } else {
                    Log.i("ERROR_MESSAGE", "$resultMessage")
                }
            }
        },
        enabled = loginEnable,
        colors = ButtonDefaults.buttonColors(containerColor = Primario),
        shape = AppShapes.medium,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clickable { navigationToHome() }
    ) {
        Text("Acceder", fontSize = 16.sp, color = Color.White)
    }
}
