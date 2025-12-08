package com.bubba.express.data.repository

import com.bubba.express.domain.model.Order

interface OrderRepository {
    suspend fun getOrders(): List<Order>
    suspend fun getOrderById(id: Int): Order
    suspend fun createOrder(usuarioId: Int, productos: List<Pair<Int, Int>>): Order
}