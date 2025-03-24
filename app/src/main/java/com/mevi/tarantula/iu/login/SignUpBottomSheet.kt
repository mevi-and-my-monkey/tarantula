package com.mevi.tarantula.iu.login

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.tarantula.R
import com.mevi.tarantula.components.generics.CustomTextField
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpBottomSheet(
    onDismiss: () -> Unit,
    onRegister: (String, String, String, String) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        var name by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var confirmPassword by rememberSaveable { mutableStateOf("") }
        var phone by rememberSaveable { mutableStateOf("") }
        var isChecked by rememberSaveable { mutableStateOf(false) }
        var passwordVisibility by rememberSaveable { mutableStateOf(false) }
        var passwordVisibilityConfirm by rememberSaveable { mutableStateOf(false) }

        val isNameValid = name.length in 1..30
        val isEmailValid = email.length in 1..30
        val isPasswordValid = password.length in 7..12
        val isConfirmPasswordValid = confirmPassword == password
        val isFormValid =
            isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid && isChecked

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal
            )
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre Completo",
                leadingIcon = Icons.Default.Person,
                contentDescription = "Nombre",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = null,
                error = !isNameValid
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo Electrónico",
                leadingIcon = Icons.Default.Email,
                contentDescription = "Email",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = null,
                error = !isEmailValid
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                placeholder = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    val imagen = if (passwordVisibility) {
                        R.drawable.ic_eye
                    } else {
                        R.drawable.ic_eye_closed
                    }
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painterResource(imagen),
                            contentDescription = "show password",
                            tint = Primario
                        )
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
                    unfocusedBorderColor = Secundario,
                    unfocusedLabelColor =  if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
                    focusedLabelColor =  if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
                ),
                isError = !isPasswordValid
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirmar contraseña") },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    val imagen = if (passwordVisibilityConfirm) {
                        R.drawable.ic_eye
                    } else {
                        R.drawable.ic_eye_closed
                    }
                    IconButton(onClick = {
                        passwordVisibilityConfirm = !passwordVisibilityConfirm
                    }) {
                        Icon(
                            painterResource(imagen),
                            contentDescription = "show password",
                            tint = Primario
                        )
                    }
                },
                visualTransformation = if (passwordVisibilityConfirm) {
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
                    unfocusedBorderColor = Secundario,
                    unfocusedLabelColor =  if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
                    focusedLabelColor =  if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario
                ),
                isError = !isConfirmPasswordValid
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Teléfono (Opcional)",
                leadingIcon = Icons.Default.Phone,
                contentDescription = "Teléfono",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Phone,
                error = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Primario,
                        uncheckedColor = Secundario
                    )
                )
                Text(
                    text = "Acepto los términos y condiciones",
                    fontSize = 14.sp,
                    color = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onRegister(name, email, password, phone)
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion { onDismiss() }
                    if (isFormValid) {
                        onRegister(name, email, password, phone)
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion { onDismiss() }

                    } else {
                        // Manejo de errores
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Secundario else Primario,
                    contentColor = Color.White
                )
            ) {
                Text("Registrar")
            }

            TextButton(
                onClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion { onDismiss() }
                }
            ) {
                Text("Cancelar", color = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario)
            }
        }
    }
}