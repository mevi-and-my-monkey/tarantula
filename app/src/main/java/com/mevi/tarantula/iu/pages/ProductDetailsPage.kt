package com.mevi.tarantula.iu.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.User
import com.mevi.tarantula.core.GlobalNavigation
import com.mevi.tarantula.core.ProductEdit
import com.mevi.tarantula.network.ProductModel
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD
import com.mevi.tarantula.utils.Utilities
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun ProductDetailsPage(modifier: Modifier = Modifier, productId: String) {
    var product by remember { mutableStateOf(ProductModel()) }

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock").collection("products")
            .document(productId).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(ProductModel::class.java)
                    if (result != null) {
                        product = result
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                modifier = Modifier.padding(8.dp)
            )

            if (User.userAdmin && !User.userInvited) {
                IconButton(onClick = {
                    GlobalNavigation.navContoller.navigate("$ProductEdit/" + product.id)
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar opciones",
                        tint = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            val pageStart = rememberPagerState(0) {
                product.images.size
            }
            HorizontalPager(state = pageStart, pageSpacing = 24.dp) {
                Box(
                    modifier = Modifier
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    SubcomposeAsyncImage(
                        model = Utilities.getDirectDriveImageUrl(product.images[it]),
                        contentDescription = "Product images",
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Gray.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            DotsIndicator(
                dotCount = product.images.size,
                pagerState = pageStart,
                type = ShiftIndicatorType(
                    DotGraphic(color = TextoSecundarioD, size = 6.dp)
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (product.price.isNotEmpty()) {
                Text(
                    text = "$" + product.price,
                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
                    style = TextStyle(textDecoration = TextDecoration.LineThrough)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$" + product.actualPrice,
                fontSize = 20.sp,
                color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Add to Favorite"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                Utilities.addItemToCart(productId, context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Fondo,
                contentColor = Color.Black
            ),
        ) {
            Text(
                "Agregar al carrito",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Descripción : ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.description,
            fontSize = 16.sp,
            color = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (product.otherDetails.isNotEmpty()) {
            Text(
                text = "Otros detalles : ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
            )
            Spacer(modifier = Modifier.height(8.dp))
            product.otherDetails.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(
                        text = "$key : ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                    )
                    Text(
                        text = value,
                        fontSize = 16.sp,
                        color = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
                    )
                }
            }
        }
    }
}