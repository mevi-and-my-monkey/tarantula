package com.mevi.tarantula.iu

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mevi.tarantula.iu.pages.CartPage
import com.mevi.tarantula.iu.pages.EventsPage
import com.mevi.tarantula.iu.pages.FavoritePage
import com.mevi.tarantula.iu.pages.HomePage
import com.mevi.tarantula.iu.pages.ProfilePage
import com.mevi.tarantula.ui.theme.IndicatorNavgation
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val navItemList =
        listOf(
            NavItemList("Home", Icons.Default.Home),
            NavItemList("Favoritos", Icons.Default.Favorite),
            NavItemList("Eventos", Icons.Default.DateRange),
            NavItemList("Carrito", Icons.Default.ShoppingCart),
            NavItemList("Perfil", Icons.Default.Person)
        )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current.findActivity()

    BackHandler {
        if (selectedIndex != 0) {
            selectedIndex = 0
        } else {
            showExitDialog = true
        }
    }

    Scaffold(bottomBar = {
        NavigationBar {
            navItemList.forEachIndexed { index, navItem ->
                NavigationBarItem(
                    selected = index == selectedIndex,
                    onClick = {
                        selectedIndex = index
                    },
                    icon = {
                        Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                        unselectedTextColor = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
                        indicatorColor = IndicatorNavgation,
                        unselectedIconColor = Primario,
                        selectedIconColor = Secundario
                    ),
                    label = { Text(text = navItem.label) }
                )
            }
        }
    }) {
        ContentScreen(modifier = modifier.padding(it), selectedIndex, navController)
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Salir de la aplicación") },
            text = { Text("¿Estás seguro que deseas salir?") },
            confirmButton = {
                TextButton(onClick = { activity?.finish() }) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

}

@Composable
fun ContentScreen(modifier: Modifier, selectedIndex: Int, navController: NavController) {
    when (selectedIndex) {
        0 -> HomePage(modifier.padding(horizontal = 8.dp))
        1 -> FavoritePage(modifier.padding(horizontal = 8.dp))
        2 -> EventsPage(modifier.padding(horizontal = 8.dp))
        3 -> CartPage(modifier.padding(horizontal = 8.dp))
        4 -> ProfilePage(modifier.padding(horizontal = 8.dp), navController)
    }
}

data class NavItemList(
    val label: String,
    val icon: ImageVector
)

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}