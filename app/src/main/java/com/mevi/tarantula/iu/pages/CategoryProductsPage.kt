package com.mevi.tarantula.iu.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun CategoryProductsPage(modifier: Modifier, navController: NavHostController, categoryId: String){
    Text(text = "Category produc page  :::::: $categoryId")
}