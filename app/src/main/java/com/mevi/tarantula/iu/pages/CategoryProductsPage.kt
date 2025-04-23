package com.mevi.tarantula.iu.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.components.ProductItemView
import com.mevi.tarantula.network.CategoryModel
import com.mevi.tarantula.network.ProductModel

@Composable
fun CategoryProductsPage(modifier: Modifier = Modifier, navController: NavHostController, categoryId: String){

    val productList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    LaunchedEffect(key1 = Unit) {
        Firebase.firestore.collection("data").document("stock").collection("products")
            .whereEqualTo("category", categoryId)
            .get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    val resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }
                    productList.value = resultList
                }
            }
    }

    LazyColumn (modifier.fillMaxSize().padding(16.dp)) {
        items(productList.value.chunked(3) ){ rowItems ->
            Row {
                rowItems.forEach { rowItems ->
                    ProductItemView(product = rowItems, modifier = Modifier.weight(1f))
                }
                if (rowItems.size == 1){
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

}