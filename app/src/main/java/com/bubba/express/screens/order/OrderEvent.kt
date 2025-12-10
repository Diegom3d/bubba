package com.bubba.express.screens.order

import com.bubba.express.domain.model.CartItem
import com.bubba.express.domain.model.Product

sealed class OrderEvent {
    data class AddToCart(val product: Product) : OrderEvent()
    data class RemoveFromCart(val item: CartItem) : OrderEvent()
    data class UpdateQuantity(val item: CartItem, val newQuantity: Int) : OrderEvent()
    data class ConfirmOrder(val usuarioId: Int) : OrderEvent()
    object ClearCart : OrderEvent()
}