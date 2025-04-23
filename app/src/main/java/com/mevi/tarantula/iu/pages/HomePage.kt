package com.mevi.tarantula.iu.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.tarantula.components.BannerView
import com.mevi.tarantula.components.CategoriesView
import com.mevi.tarantula.components.HeaderView
import com.mevi.tarantula.components.ShowAllProductsView

@Composable
fun HomePage(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    )
    {
        HeaderView(modifier)
        Spacer(modifier = Modifier.height(10.dp))
        BannerView(modifier = Modifier.height(220.dp))
        Text("Categorias", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(10.dp))
        CategoriesView()
        Spacer(modifier = Modifier.height(10.dp))
        ShowAllProductsView()
    }
}