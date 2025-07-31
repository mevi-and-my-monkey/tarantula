package com.mevi.tarantula.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoriesMap = mapOf(
        "cuento" to "Cuento",
        "ensayoLiterario" to "Ensayo literario",
        "fiction" to "Ficción",
        "literaturaIberoamericana" to "Literatura iberoamericana",
        "literaturaMexicana" to "Literatura mexicana",
        "literaturaUniversal" to "Literatura universal",
        "novelasIngles" to "Novelas en inglés",
        "poesia" to "Poesía",
        "teatro" to "Teatro",
        "teoriaLiteraria" to "Teoría literaria"
    )

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = categoriesMap[selectedCategory] ?: "",
            onValueChange = {},
            label = { Text("Categoría") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
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

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoriesMap.forEach { (key, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onCategorySelected(key)
                        expanded = false
                    }
                )
            }
        }
    }
}