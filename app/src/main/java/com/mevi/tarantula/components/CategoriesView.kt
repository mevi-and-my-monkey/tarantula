package com.mevi.tarantula.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.core.CategoryProducts
import com.mevi.tarantula.core.GlobalNavigation
import com.mevi.tarantula.network.CategoryModel
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.Primario
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD
import com.mevi.tarantula.utils.Utilities

@Composable
fun CategoriesView(modifier: Modifier = Modifier) {
    val categoryList = remember {
        mutableStateOf<List<CategoryModel>>(emptyList())
    }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock").collection("categories")
            .get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    val resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(CategoryModel::class.java)
                    }
                    categoryList.value = resultList
                }
            }
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        items(categoryList.value) { item ->
            CategoryItem(category = item)
        }
    }
}

@Composable
fun CategoryItem(category: CategoryModel) {
    Card(
        modifier = Modifier
            .size(90.dp)
            .clickable {
                GlobalNavigation.navContoller.navigate("${CategoryProducts}/" + category.id)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(contentColor = if (isSystemInDarkTheme()) Fondo else Color.White,)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Fondo else Color.White)
        ) {
            SubcomposeAsyncImage(
                model = Utilities.getDirectDriveImageUrl(category.imageUrl),
                contentDescription = category.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(50.dp),
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                textAlign = TextAlign.Center,
                color = if (isSystemInDarkTheme()) Color.Black else TextoPrincipal,
                fontSize = 12.sp,
                maxLines = 2, // Limita a 2 líneas
                overflow = TextOverflow.Ellipsis, // Agrega "..." si se excede
                modifier = Modifier
                    .padding(horizontal = 4.dp) // Espacio para evitar que el texto toque los bordes
            )
        }
    }
}