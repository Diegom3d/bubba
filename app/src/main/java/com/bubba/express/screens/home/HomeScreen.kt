package com.bubba.express.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bubba.express.domain.model.Product
import com.bubba.express.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showProfileMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "La Bubba Express",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = CoffeePrimary
                        )
                        Text(
                            "Café de especialidad",
                            fontSize = 12.sp,
                            color = GrayMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WhitePure
                ),
                actions = {
                    // Carrito
                    IconButton(onClick = onCartClick) {
                        BadgedBox(
                            badge = {
                                if (state.cartItemCount > 0) {
                                    Badge(
                                        containerColor = CoffeePrimary
                                    ) {
                                        Text(
                                            "${state.cartItemCount}",
                                            color = WhitePure,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = CoffeePrimary
                            )
                        }
                    }

                    // Perfil
                    Box {
                        IconButton(onClick = { showProfileMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Perfil",
                                tint = CoffeePrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showProfileMenu,
                            onDismissRequest = { showProfileMenu = false },
                            modifier = Modifier
                                .background(WhitePure)
                                .width(220.dp)
                        ) {
                            // Nombre de usuario
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = state.currentUser?.nombre ?: "Usuario",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Black
                                )
                                Text(
                                    text = state.currentUser?.email ?: "",
                                    fontSize = 12.sp,
                                    color = GrayMedium
                                )
                            }

                            Divider(color = GrayLight)

                            // Mi perfil
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.Person,
                                            contentDescription = null,
                                            tint = CoffeePrimary
                                        )
                                        Text("Mi Perfil")
                                    }
                                },
                                onClick = {
                                    showProfileMenu = false
                                    onProfileClick()
                                }
                            )

                            // Historial de pedidos
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.History,
                                            contentDescription = null,
                                            tint = CoffeePrimary
                                        )
                                        Text("Historial de Pedidos")
                                    }
                                },
                                onClick = {
                                    showProfileMenu = false
                                    onHistoryClick()
                                }
                            )

                            Divider(color = GrayLight)

                            // Cerrar sesión
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.Logout,
                                            contentDescription = null,
                                            tint = ErrorRed
                                        )
                                        Text(
                                            "Cerrar Sesión",
                                            color = ErrorRed
                                        )
                                    }
                                },
                                onClick = {
                                    showProfileMenu = false
                                    onLogoutClick()
                                }
                            )
                        }
                    }
                }
            )
        },
        containerColor = CreamBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = CoffeePrimary
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            color = ErrorRed,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.onEvent(HomeEvent.LoadProducts) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CoffeePrimary
                            )
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Banner promocional
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CoffeePrimary
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "¡Bienvenido ${state.currentUser?.nombre?.split(" ")?.firstOrNull() ?: ""}!",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = WhitePure
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Descubre nuestras especialidades",
                                        fontSize = 14.sp,
                                        color = WhitePure.copy(alpha = 0.9f)
                                    )
                                }
                                Text(
                                    "☕",
                                    fontSize = 48.sp
                                )
                            }
                        }

                        // Categorías
                        Text(
                            "Nuestro Menú",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        // Grid de productos
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.products) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = { onProductClick(product.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhitePure
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Emoji basado en categoría
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(CreamBackground),
                contentAlignment = Alignment.Center
            ) {
                product.categoria?.let {
                    Text(
                        text = getEmojiForCategory(it),
                        fontSize = 56.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.nombre,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = Black,
                maxLines = 2,
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = CoffeePrimary,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "$${String.format("%.2f", product.precio)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = WhitePure,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            if (!product.disponible) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "No disponible",
                    fontSize = 11.sp,
                    color = ErrorRed
                )
            }
        }
    }
}

private fun getEmojiForCategory(categoria: String): String {
    return when (categoria.lowercase()) {
        "cafe", "coffee", "café" -> "☕"
        "te", "tea", "té" -> "🍵"
        "postre", "dessert" -> "🍰"
        "pasteleria", "pastry" -> "🥐"
        "sandwich" -> "🥪"
        "bebida", "drink" -> "🥤"
        else -> "☕"
    }
}