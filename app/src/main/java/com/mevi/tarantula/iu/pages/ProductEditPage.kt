package com.mevi.tarantula.iu.pages

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.components.CategoryDropdown
import com.mevi.tarantula.components.generics.CustomOutlinedTextField
import com.mevi.tarantula.core.GlobalNavigation
import com.mevi.tarantula.core.Home
import com.mevi.tarantula.network.ProductModel
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.Secundario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.ui.theme.TextoSecundario
import com.mevi.tarantula.ui.theme.TextoSecundarioD

@Composable
fun ProductEditPage(modifier: Modifier = Modifier, productId: String) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(true) }
    var product by remember { mutableStateOf(ProductModel()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Campos editables
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var actualPrice by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var pages by remember { mutableStateOf("") }
    var publicationDate by remember { mutableStateOf("") }
    var imageUrls by remember { mutableStateOf("") }

    LaunchedEffect(productId) {
        Firebase.firestore.collection("data").document("stock")
            .collection("products").document(productId).get()
            .addOnSuccessListener { doc ->
                val p = doc.toObject(ProductModel::class.java)
                if (p != null) {
                    product = p
                    title = p.title
                    description = p.description
                    price = p.price
                    actualPrice = p.actualPrice
                    category = p.category
                    author = p.author
                    language = p.language
                    pages = p.pages
                    publicationDate = p.publicationDate
                    imageUrls = p.images.joinToString("\n")
                }
                loading = false
            }
    }

    if (loading) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Editar producto", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))

            CustomOutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = "Título",
                capitalizeFirst = true
            )
            CustomOutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = "Descripción",
                capitalizeFirst = true
            )
            CustomOutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = "Precio original",
                keyboardType = KeyboardType.Number,
                maxLength = 10
            )
            CustomOutlinedTextField(
                value = actualPrice,
                onValueChange = { actualPrice = it },
                label = "Precio actual",
                keyboardType = KeyboardType.Number,
                maxLength = 10
            )
            CategoryDropdown(
                selectedCategory = category,
                onCategorySelected = { category = it }
            )
            CustomOutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = "Autor",
                capitalizeFirst = true
            )
            CustomOutlinedTextField(
                value = language,
                onValueChange = { language = it },
                label = "Idioma",
                capitalizeFirst = true
            )
            CustomOutlinedTextField(
                value = pages,
                onValueChange = { pages = it },
                label = "Número de páginas",
                keyboardType = KeyboardType.Number,
                maxLength = 10
            )
            CustomOutlinedTextField(
                value = publicationDate,
                onValueChange = { publicationDate = it },
                label = "Fecha de publicación",
                keyboardType = KeyboardType.Number,
                maxLength = 4
            )
            OutlinedTextField(
                value = imageUrls,
                onValueChange = { imageUrls = it },
                label = { Text("URLs de imágenes (una por línea)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = if (isSystemInDarkTheme()) TextoSecundarioD else TextoSecundario,
                    focusedTextColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Primario,
                    focusedBorderColor = Primario,
                    unfocusedBorderColor = Secundario,
                    unfocusedLabelColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                    focusedLabelColor = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val updatedProduct = product.copy(
                        title = title.trim(),
                        description = description.trim(),
                        price = price.trim(),
                        actualPrice = actualPrice.trim(),
                        category = category.trim(),
                        author = author.trim(),
                        language = language.trim(),
                        pages = pages.trim(),
                        publicationDate = publicationDate.trim(),
                        images = imageUrls.lines().map { it.trim() }.filter { it.isNotEmpty() }
                    )

                    Firebase.firestore.collection("data").document("stock")
                        .collection("products").document(productId)
                        .set(updatedProduct)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT)
                                .show()
                            GlobalNavigation.navContoller.popBackStack()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
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
                    "Guardar cambios",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    showDeleteDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Eliminar producto",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar producto?") },
            text = { Text("Esta acción no se puede deshacer. ¿Estás seguro que deseas eliminar este producto?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    Firebase.firestore.collection("data").document("stock")
                        .collection("products").document(productId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                            GlobalNavigation.navContoller.navigate(Home) {
                                popUpTo<Home> { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
