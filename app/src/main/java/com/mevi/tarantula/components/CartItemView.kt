package com.mevi.tarantula.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.network.ProductModel
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.utils.Utilities

@Composable
fun CartItemView(modifier: Modifier = Modifier, productId: String, qyt: Long) {
    var product by remember { mutableStateOf(ProductModel()) }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock").collection("products")
            .document(productId).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(ProductModel::class.java)
                    if (result != null) {
                        product = result
                    }
                }
            }
    }

    val context = LocalContext.current
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Fondo else Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = Utilities.getDirectDriveImageUrl(product.images.firstOrNull() ?: ""),
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                if (isSystemInDarkTheme()) Fondo.copy(alpha = 0.3f) else Color.White.copy(
                                    alpha = 0.3f
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primario)
                    }
                }
            )

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "$" + product.actualPrice,
                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        Utilities.removeFromCart(productId, context)
                    }) {
                        Text(text = "-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(text = "$qyt", fontSize = 16.sp)

                    IconButton(onClick = {
                        Utilities.addItemToCart(productId, context)
                    }) {
                        Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            IconButton(onClick = {
                Utilities.removeFromCart(productId, context, removeAll = true)
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Add to cart",
                    tint = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                )
            }
        }
    }

}