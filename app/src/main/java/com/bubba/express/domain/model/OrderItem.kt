package com.bubba.express.domain.model

data class OrderItem(
    val id: Int,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double
)