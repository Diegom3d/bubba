package com.bubba.express.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bubba.express.domain.model.Product
import com.bubba.express.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    onAddToCart: (String) -> Unit
) {
    var customization by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Producto",
                        fontWeight = FontWeight.SemiBold
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen grande del producto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(WhitePure),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(CreamBackground),
                    contentAlignment = Alignment.Center
                ) {
                    product.categoria?.let {
                        Text(
                            text = getEmojiForCategory(it),
                            fontSize = 120.sp
                        )
                    }
                }
            }

            // Card de información
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = WhitePure
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Nombre y precio
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.nombre,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Black,
                            modifier = Modifier.weight(1f)
                        )

                        Surface(
                            color = CoffeePrimary,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "$${String.format("%.2f", product.precio)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = WhitePure,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Categoría
                    Surface(
                        color = CoffeePrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        product.categoria?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = CoffeePrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción
                    Text(
                        text = "Descripción",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CoffeeSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    product.descripcion?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = GrayDark,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo de personalización
                    Text(
                        text = "Personaliza tu bebida",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CoffeeSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = customization,
                        onValueChange = { customization = it },
                        placeholder = {
                            Text(
                                "Ej: Sin azúcar, leche de almendra...",
                                color = GrayMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CoffeePrimary,
                            unfocusedBorderColor = GrayLight,
                            focusedContainerColor = CreamBackground,
                            unfocusedContainerColor = CreamBackground
                        ),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón agregar
                    Button(
                        onClick = { onAddToCart(customization) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoffeePrimary
                        ),
                        enabled = product.disponible
                    ) {
                        Text(
                            text = if (product.disponible) "Agregar al Carrito" else "No Disponible",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
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