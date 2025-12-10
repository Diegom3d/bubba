package com.bubba.express.screens.order

import com.bubba.express.domain.model.CartItem
import com.bubba.express.domain.model.Order

data class OrderState(
    val isLoading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val confirmedOrder: Order? = null,
    val error: String? = null
) {
    val total: Double
        get() = cartItems.sumOf { it.product.precio * it.cantidad }
}