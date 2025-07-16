package com.mevi.tarantula.iu.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.User
import com.mevi.tarantula.components.CartItemView
import com.mevi.tarantula.core.CheckOut
import com.mevi.tarantula.core.GlobalNavigation
import com.mevi.tarantula.network.UserModel
import com.mevi.tarantula.ui.theme.Fondo

@Composable
fun CartPage(modifier: Modifier = Modifier) {
    if (!User.userInvited) {
        val userModel = remember {
            mutableStateOf(UserModel())
        }

        DisposableEffect(key1 = Unit) {
            val listener = Firebase.firestore.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .addSnapshotListener { it, _ ->
                    if (it != null) {
                        val result = it.toObject(UserModel::class.java)
                        if (result != null) {
                            userModel.value = result
                        }
                    }
                }

            onDispose {
                listener.remove()
            }
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = ("Tu carrito"),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )

            if (userModel.value.cartItems.isNotEmpty()) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(
                        userModel.value.cartItems.toList(),
                        key = { it.first }) { (productId, qyt) ->
                        CartItemView(productId = productId, qyt = qyt)
                    }
                }

                Button(
                    onClick = {
                        GlobalNavigation.navContoller.navigate(CheckOut)
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
                        "Calcular pedido",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No hay productos", fontSize = 32.sp)
                }
            }

        }
    } else {
        LoginRequiredScreen()
    }
}