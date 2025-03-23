package com.mevi.tarantula.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// 🎨 colores personalizados
val Fondo = Color(0xFFE4CAAF)
val Primario = Color(0xFF8B5E3C)
val Secundario = Color(0xFFC98A5F)
val Acento = Color(0xFF5F3829)
val TextoPrincipal = Color(0xFF3D2B1F)
val TextoSecundario = Color(0xFF705A4A)
val TextoPrincipalD = Color(0xFFC4BBB4)
val TextoSecundarioD = Color(0xFF938883)
val Exito = Color(0xFF4CAF50)
val Error = Color(0xFFD32F2F)
val Advertencia = Color(0xFFFFA000)
val IndicatorNavgation = Color(0x33D4A474)

// esquema de colores claro y oscuro
val LightColorScheme = lightColorScheme(
    primary = Primario,
    secondary = Secundario,
    background = Fondo,
    surface = Fondo,
    onPrimary = Color.White,
    onSecondary = TextoPrincipal,
    onBackground = TextoPrincipal,
    onSurface = TextoSecundario,
    error = Error,
    onError = Color.White
)

val DarkColorScheme = darkColorScheme(
    primary = Primario,
    secondary = Secundario,
    background = Color(0xFF2B2B2B),
    surface = Color(0xFF3E3E3E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color(0xFFBDBDBD),
    error = Error,
    onError = Color.White
)

// Formas personalizadas
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // Detalles pequeños
    small = RoundedCornerShape(8.dp),       // Botones pequeños, chips
    medium = RoundedCornerShape(16.dp),     // Tarjetas y contenedores
    large = RoundedCornerShape(24.dp),      // Diálogos, pop-ups
    extraLarge = RoundedCornerShape(32.dp)  // Elementos grandes
)


@Composable
fun TarantulaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}