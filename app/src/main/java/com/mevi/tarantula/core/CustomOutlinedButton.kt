package com.mevi.tarantula.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mevi.tarantula.ui.theme.AppShapes
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario

@Composable
fun CustomOutlinedButton(
    onClick: () -> Unit,
    text: String,
    iconResId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = AppShapes.medium,
        modifier = modifier.fillMaxWidth(0.8f),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoSecundario),
        border = BorderStroke(2.dp, Primario)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            tint = Primario,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoSecundario,)
    }
}