package com.mevi.tarantula.components.generics

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int = Int.MAX_VALUE,
    capitalizeFirst: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            capitalization = if (capitalizeFirst) KeyboardCapitalization.Sentences else KeyboardCapitalization.None,
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
            focusedTextColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Primario,
            focusedBorderColor = Primario,
            unfocusedBorderColor = Secundario,
            unfocusedLabelColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
            focusedLabelColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal
        )
    )
}