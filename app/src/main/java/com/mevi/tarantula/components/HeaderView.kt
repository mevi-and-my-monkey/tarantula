package com.mevi.tarantula.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.R
import com.mevi.tarantula.User
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD

@Composable
fun HeaderView(modifier: Modifier = Modifier) {

    var name by rememberSaveable { mutableStateOf("") }
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val photoUrl = firebaseUser?.photoUrl?.toString()

    if (!User.userInvited) {
        LaunchedEffect(Unit) {
            Firebase.firestore.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .get().addOnCompleteListener {
                    name = it.result.get("name").toString().split(" ")[0]
                }
        }
    } else {
        name = "Invitado"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl)
                    .crossfade(true)
                    .error(R.drawable.ic_guest)
                    .placeholder(R.drawable.ic_guest)
                    .build(),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(if (!User.userInvited) "Bienvenido de nuevo" else "Bienvenido", color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal)
                Text(text = name, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold), color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal)
            }
        }

        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
    }
}