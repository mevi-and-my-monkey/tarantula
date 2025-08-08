package com.mevi.tarantula.components.generics

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    contentDescription: String,
    keyboardType: KeyboardType?,
    modifier: Modifier,
    error: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = contentDescription,
                tint = Primario
            )
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType ?: KeyboardType.Text),
        modifier = modifier,
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
        isError = error
    )
}