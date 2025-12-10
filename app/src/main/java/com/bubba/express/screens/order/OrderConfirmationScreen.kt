package com.bubba.express.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import com.bubba.express.ui.theme.*

@Composable
fun OrderConfirmationScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onNewOrder: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val order = state.confirmedOrder

    if (order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = CoffeePrimary)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Icono de éxito
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(SuccessGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = WhitePure,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Pedido Confirmado!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu pedido está siendo preparado",
            fontSize = 16.sp,
            color = GrayMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Card con código
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = WhitePure
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Código de Recogida",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = GrayMedium,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Código destacado con ID del pedido
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CoffeePrimary
                ) {
                    Text(
                        text = "#${order.id.toString().padStart(6, '0')}",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhitePure,
                        letterSpacing = 6.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(color = GrayLight)

                Spacer(modifier = Modifier.height(20.dp))

                // Título resumen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resumen del Pedido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Text(
                        text = "${order.items.size} items",
                        fontSize = 14.sp,
                        color = GrayMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de items
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = CreamBackground,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${item.cantidad}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CoffeePrimary
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.nombreProducto,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Black
                                    )
                                }
                            }

                            Text(
                                text = "$${String.format("%.2f", item.subtotal)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CoffeePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = GrayLight)

                Spacer(modifier = Modifier.height(16.dp))

                // Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Pagado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Text(
                        text = "$${String.format("%.2f", order.total)}",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoffeePrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Estado del pedido
                Surface(
                    color = when (order.estado.lowercase()) {
                        "pendiente" -> CoffeeAccent.copy(alpha = 0.2f)
                        "en preparación", "en preparacion" -> CoffeePrimary.copy(alpha = 0.2f)
                        "completado" -> SuccessGreen.copy(alpha = 0.2f)
                        else -> GrayLight
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Estado: ${order.estado}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (order.estado.lowercase()) {
                            "pendiente" -> CoffeeAccent
                            "en preparación", "en preparacion" -> CoffeePrimary
                            "completado" -> SuccessGreen
                            else -> GrayDark
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Información adicional
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CoffeePrimary.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ℹ️",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Presenta este código en la barra para recoger tu pedido",
                    fontSize = 13.sp,
                    color = GrayDark,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón nuevo pedido
        Button(
            onClick = {
                viewModel.onEvent(OrderEvent.ClearCart)
                onNewOrder()
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
                text = "Realizar Nuevo Pedido",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}