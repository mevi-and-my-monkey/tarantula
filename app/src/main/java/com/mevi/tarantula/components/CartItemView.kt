package com.mevi.tarantula.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CartItemView(modifier: Modifier = Modifier, productId:String, qyt: Long){
    Text("$productId ->>>>>> $qyt")
}