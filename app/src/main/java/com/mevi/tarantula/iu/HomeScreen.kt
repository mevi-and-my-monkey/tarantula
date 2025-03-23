package com.mevi.tarantula.iu

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
            NavItemList("Perfil", Icons.Default.Person)
        )

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

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
                        selectedTextColor = if (isSystemInDarkTheme()) TextoPrincipalD  else TextoPrincipal,
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

}

@Composable
fun ContentScreen(modifier: Modifier, selectedIndex: Int, navController: NavController) {
    when (selectedIndex) {
        0 -> HomePage(modifier.padding(horizontal = 8.dp))
        1 -> FavoritePage(modifier.padding(horizontal = 8.dp))
        2 -> EventsPage(modifier.padding(horizontal = 8.dp))
        3 -> ProfilePage(modifier.padding(horizontal = 8.dp), navController)
    }
}

data class NavItemList(
    val label: String,
    val icon: ImageVector
)