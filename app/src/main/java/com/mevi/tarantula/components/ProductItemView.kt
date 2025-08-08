package com.mevi.tarantula.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.mevi.tarantula.core.GlobalNavigation
import com.mevi.tarantula.core.ProductDetails
import com.mevi.tarantula.network.ProductModel
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.utils.Utilities

@Composable
fun ProductItemView(modifier: Modifier = Modifier, product: ProductModel) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                GlobalNavigation.navContoller.navigate("${ProductDetails}/" + product.id)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Fondo else Color.White,),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            SubcomposeAsyncImage(
                model = Utilities.getDirectDriveImageUrl(product.images.firstOrNull() ?: ""),
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(if (isSystemInDarkTheme()) Fondo.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primario)
                    }
                }
            )

            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.price.isNotEmpty()) {
                    Text(
                        text = "$" + product.price,
                        fontSize = 14.sp,
                        color = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$" + product.actualPrice,
                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    Utilities.addItemToCart(product.id, context)
                }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to cart",
                        tint = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                        )
                }
            }
        }
    }
}