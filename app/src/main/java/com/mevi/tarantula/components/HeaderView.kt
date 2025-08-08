package com.mevi.tarantula.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.mevi.tarantula.core.AddProduct
import com.mevi.tarantula.core.GlobalNavigation
import com.mevi.tarantula.ui.theme.TextoPrincipal
import com.mevi.tarantula.ui.theme.TextoPrincipalD

@Composable
fun HeaderView(modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val photoUrl = firebaseUser?.photoUrl?.toString()
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

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
                Text(
                    if (!User.userInvited) "Bienvenido de nuevo" else "Bienvenido",
                    color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal
                )
                Text(
                    text = name,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    color = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal
                )
            }
        }

        if (User.userAdmin && !User.userInvited) {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Más opciones",
                        tint = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .width(200.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = null,
                                    tint = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Agregar producto", fontSize = 16.sp)
                            }
                        },
                        onClick = {
                            menuExpanded = false
                            GlobalNavigation.navContoller.navigate(AddProduct)
                        },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = if (isSystemInDarkTheme()) TextoPrincipalD else TextoPrincipal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Agregar evento", fontSize = 16.sp)
                            }
                        },
                        onClick = {
                            menuExpanded = false
                        },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}