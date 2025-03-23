package com.mevi.tarantula.iu.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.tarantula.components.BannerView
import com.mevi.tarantula.components.HeaderView

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
    }
}