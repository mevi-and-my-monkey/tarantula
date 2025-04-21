package com.mevi.tarantula.iu

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mevi.tarantula.R
import com.mevi.tarantula.User
import com.mevi.tarantula.core.CustomOutlinedButton
import com.mevi.tarantula.iu.login.LoginViewModel
import com.mevi.tarantula.iu.login.SignUpBottomSheet
import com.mevi.tarantula.ui.theme.AppShapes
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD
import com.mevi.tarantula.utils.Utilities

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
        }
    }
}

@Composable
fun Body(loginViewModel: LoginViewModel, navigationToHome: () -> Unit, modifier: Modifier) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            loginViewModel.showLoading()
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                loginViewModel.signInWithGoogleCredential(credential) { success, resultMessage ->
                    if (success) {
                        User.userAdmin = Utilities.isAdmin(loginViewModel, account.email?:"Sin dato")
                        User.userInvited = false
                        loginViewModel.hideLoading()
                        navigationToHome()
                    } else {
                        loginViewModel.hideLoading()
                        Toast.makeText(context, "$resultMessage", Toast.LENGTH_LONG).show()
                        Log.i("ERROR_MESSAGE", "$resultMessage")
                    }
                }
            } catch (e: Exception) {
                Log.d("Google_aut", "Google SignIn fallo")
            }

        }
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
            color = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
            fontSize = 14.sp,
            modifier = Modifier.clickable { /* Acción de recuperación */ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LoginButton(loginViewModel.isLoginEnable, navigationToHome, loginViewModel)
        Spacer(modifier = Modifier.height(8.dp))
        CustomOutlinedButton(
            onClick = {
                val opciones = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInCliente = GoogleSignIn.getClient(context, opciones)
                launcher.launch(googleSignInCliente.signInIntent)
            },
            "Continua con Google",
            iconResId = R.drawable.ic_google,
            "Google",
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomOutlinedButton(
            onClick = {
                User.userInvited = true
                navigationToHome()
            },
            "Continua como invitado",
            R.drawable.ic_guest,
            "Invitado",
            Modifier
        )
    }
}

@Composable
fun Footer(modifier: Modifier, loginViewModel: LoginViewModel, navigationToHome: () -> Unit) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        SignUp(modifier, loginViewModel, navigationToHome)
    }
}

@Composable
fun SignUp(modifier: Modifier, loginViewModel: LoginViewModel, navigationToHome: () -> Unit) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("¿No tienes una cuenta?", color = TextoSecundario, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Crear cuenta",
            color = TextoPrincipal,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                showBottomSheet = true
            }
        )
    }
    Spacer(modifier = modifier.height(8.dp))
    Text(
        "Tarantula by Mevi®",
        color = TextoSecundario,
        fontSize = 12.sp,
        textAlign = TextAlign.Center, modifier = modifier.fillMaxWidth()
    )
    Spacer(modifier = modifier.height(8.dp))

    if (showBottomSheet) {
        SignUpBottomSheet(
            onDismiss = { showBottomSheet = false },
            onRegister = { name, email, password, phone ->
                loginViewModel.showLoading()
                loginViewModel.signUp(name, email, password, phone) { success, resultMessage ->
                    if (success) {
                        User.userAdmin = Utilities.isAdmin(loginViewModel, email)
                        loginViewModel.hideLoading()
                        navigationToHome()
                    } else {
                        loginViewModel.hideLoading()
                        Toast.makeText(context, "$resultMessage", Toast.LENGTH_LONG).show()
                        Log.i("ERROR_MESSAGE", "$resultMessage")
                    }
                }
                showBottomSheet = false
            }
        )
    }
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
                Icons.Default.Email,
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
            unfocusedTextColor = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
            focusedTextColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
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
                R.drawable.ic_eye
            } else {
                R.drawable.ic_eye_closed
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(painterResource(imagen), contentDescription = "show password", tint = Primario)
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
            unfocusedTextColor = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
            focusedTextColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
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
    navigationToHome: () -> Unit,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    Button(
        onClick = {
            loginViewModel.showLoading()
            loginViewModel.login(
                loginViewModel.email,
                loginViewModel.password
            ) { success, resultMessage ->
                if (success) {
                    User.userAdmin = Utilities.isAdmin(loginViewModel, loginViewModel.email)
                    User.userInvited = false
                    loginViewModel.hideLoading()
                    navigationToHome()
                } else {
                    loginViewModel.hideLoading()
                    Toast.makeText(context, "$resultMessage", Toast.LENGTH_LONG).show()
                    Log.i("ERROR_MESSAGE", "$resultMessage")
                }
            }
        },
        enabled = loginEnable,
        colors = ButtonDefaults.buttonColors(containerColor = Primario),
        shape = AppShapes.medium,
        modifier = Modifier
            .fillMaxWidth(0.8f)
    ) {
        Text("Acceder", fontSize = 16.sp, color = Color.White)
    }
}
