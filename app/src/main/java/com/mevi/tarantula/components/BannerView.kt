package com.mevi.tarantula.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.ui.theme.Fondo
import com.mevi.tarantula.ui.theme.TextoSecundarioD
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun BannerView(modifier: Modifier = Modifier) {

    var bannerList by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("banners").get().addOnCompleteListener {
                val urls = it.result.get("urls") as? List<String> ?: emptyList()
                bannerList = urls.map { url ->
                    if (url.contains("drive.google.com")) {
                        val id = url.substringAfter("/d/").substringBefore("/")
                        "https://drive.google.com/uc?export=download&id=$id"
                    } else {
                        url
                    }
                }
            }
    }

    Column(modifier = modifier) {
        val pageStart = rememberPagerState(0) {
            bannerList.size
        }
        HorizontalPager(state = pageStart, pageSpacing = 24.dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                SubcomposeAsyncImage(
                    model = bannerList[it],
                    contentDescription = "Banner image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(if (isSystemInDarkTheme()) Fondo.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f)),
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
            dotCount = bannerList.size,
            pagerState = pageStart,
            type = ShiftIndicatorType(
                DotGraphic(color = TextoSecundarioD, size = 6.dp)
            )
        )
    }
}