package com.bubba.express.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bubba.express.domain.model.CartItem
import com.bubba.express.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    usuarioId: Int,
    onBack: () -> Unit,
    onOrderConfirmed: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Carrito",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Volver",
                            tint = CoffeePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WhitePure
                )
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
                state.cartItems.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(WhitePure),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🛒",
                                fontSize = 64.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Tu carrito está vacío",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GrayDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Agrega productos para comenzar",
                            fontSize = 14.sp,
                            color = GrayMedium
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Lista de items
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.cartItems) { item ->
                                OrderItemCard(
                                    cartItem = item,
                                    onUpdateQuantity = { newQuantity ->
                                        viewModel.onEvent(OrderEvent.UpdateQuantity(item, newQuantity))
                                    },
                                    onRemove = {
                                        viewModel.onEvent(OrderEvent.RemoveFromCart(item))
                                    }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        // Footer con total
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = WhitePure
                            ),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Subtotal",
                                        fontSize = 16.sp,
                                        color = GrayDark
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", state.total)}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = GrayDark
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Divider(color = GrayLight)

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Black
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", state.total)}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CoffeePrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Button(
                                    onClick = {
                                        viewModel.onEvent(OrderEvent.ConfirmOrder(usuarioId))
                                        onOrderConfirmed()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CoffeePrimary
                                    )
                                ) {
                                    Text(
                                        text = "Confirmar Pedido",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Mostrar error si existe
            state.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(error)
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    cartItem: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhitePure
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.product.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$${String.format("%.2f", cartItem.product.precio)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoffeePrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onUpdateQuantity(cartItem.cantidad - 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CreamBackground
                        ) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = "Disminuir",
                                tint = CoffeePrimary,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    Text(
                        text = "${cartItem.cantidad}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(24.dp)
                    )

                    IconButton(
                        onClick = { onUpdateQuantity(cartItem.cantidad + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CoffeePrimary
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Aumentar",
                                tint = WhitePure,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }

                TextButton(
                    onClick = onRemove,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = ErrorRed
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar", fontSize = 12.sp)
                }
            }
        }
    }
}