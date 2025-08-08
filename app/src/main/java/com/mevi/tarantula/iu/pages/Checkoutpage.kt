package com.mevi.tarantula.iu.pages

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.network.ProductModel
import com.mevi.tarantula.network.UserModel
import com.mevi.tarantula.utils.Utilities
import androidx.core.net.toUri
import com.mevi.tarantula.R
import com.mevi.tarantula.ui.theme.WhatsAppBoton

@Composable
fun CheckoutPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val userModel = remember { mutableStateOf(UserModel()) }
    val productList = remember { mutableStateListOf(ProductModel()) }
    val subTotal = remember { mutableFloatStateOf(0f) }
    val discount = remember { mutableFloatStateOf(0f) }
    //val tax = remember { mutableFloatStateOf(0f) }
    val total = remember { mutableFloatStateOf(0f) }

    fun calculateAndAssign() {
        subTotal.floatValue = 0f
        productList.forEach {
            if (it.actualPrice.isNotEmpty()) {
                val qty = userModel.value.cartItems[it.id] ?: 0
                subTotal.floatValue += it.actualPrice.toFloat() * qty
            }
        }
        discount.floatValue = subTotal.floatValue * (Utilities.getDiscountPercentage()) / 100
        //tax.floatValue = subTotal.floatValue * (Utilities.getTaxPercentage()) / 100
        //total.floatValue = "%.2f".format(subTotal.floatValue - discount.floatValue - tax.floatValue).toFloat()
        total.floatValue = "%.2f".format(subTotal.floatValue - discount.floatValue).toFloat()
    }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result

                        Firebase.firestore.collection("data").document("stock")
                            .collection("products")
                            .whereIn("id", userModel.value.cartItems.keys.toList())
                            .get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val resultProducts =
                                        task.result.toObjects(ProductModel::class.java)
                                    productList.addAll(resultProducts)
                                    calculateAndAssign()
                                }
                            }
                    }
                }
            }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Pedido", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Entregar a : ", fontWeight = FontWeight.SemiBold)
        Text(text = userModel.value.name ?: "Verificar nombre de usuario")
        Text(
            text = userModel.value.address ?: "Sin Domicilio registrado",
            modifier = Modifier.width(180.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Text(text = "Productos", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(productList) { product ->
                val qty = userModel.value.cartItems[product.id] ?: 0
                if (qty > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(product.title, fontWeight = FontWeight.SemiBold)
                            Text("Cantidad: $qty")
                            Text("Precio unitario: $${product.actualPrice}")
                        }
                        Text(
                            "$${"%.2f".format(product.actualPrice.toFloat() * qty)}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        RowCheckoutItems(title = "Subtotal", value = subTotal.floatValue.toString())
        Spacer(modifier = Modifier.height(16.dp))
        RowCheckoutItems(title = "Descuento", value = discount.floatValue.toString())
        //Spacer(modifier = Modifier.height(16.dp))
        //RowCheckoutItems(title = "Impuestos", value = tax.floatValue.toString())
        HorizontalDivider()
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Total del pedido",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "$${total.floatValue}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                val productsMessage = productList
                    .mapNotNull { product ->
                        val qty = userModel.value.cartItems[product.id] ?: 0
                        if (qty > 0) {
                            "x$qty ${product.title}"
                        } else null
                    }
                    .joinToString("\n")

                val customMessage = """
                    Hola, me gustaría solicitar el siguiente pedido:
                    $productsMessage
                    Total: $${total.floatValue}
                    Entregar a:
                    ${userModel.value.name ?: ""}
                """.trimIndent()

                val phoneNumber = "525514023853"
                val url = "https://wa.me/$phoneNumber?text=${Uri.encode(customMessage)}"

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = url.toUri()
                }
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WhatsAppBoton,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_whatsapp),
                contentDescription = "WhatsApp",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Solicitar pedido",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }


}

@Composable
fun RowCheckoutItems(title: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "$$value", fontSize = 18.sp)
    }
}