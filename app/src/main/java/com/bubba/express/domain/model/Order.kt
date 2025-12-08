package com.bubba.express.domain.model

data class Order(
    val id: Int,
    val usuarioId: Int,
    val total: Double,
    val estado: String,
    val items: List<OrderItem>
)
