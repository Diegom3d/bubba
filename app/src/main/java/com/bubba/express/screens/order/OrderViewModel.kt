package com.bubba.express.screens.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bubba.express.data.repository.OrderRepository
import com.bubba.express.domain.model.CartItem
import com.bubba.express.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrderState())
    val state: StateFlow<OrderState> = _state.asStateFlow()

    fun onEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.AddToCart -> addToCart(event.product)
            is OrderEvent.RemoveFromCart -> removeFromCart(event.item)
            is OrderEvent.UpdateQuantity -> updateQuantity(event.item, event.newQuantity)
            is OrderEvent.ConfirmOrder -> confirmOrder(event.usuarioId)
            is OrderEvent.ClearCart -> clearCart()
        }
    }

    private fun addToCart(product: Product) {
        _state.update { currentState ->
            val existingItem = currentState.cartItems.find {
                it.product.id == product.id
            }

            val updatedItems = if (existingItem != null) {
                currentState.cartItems.map { item ->
                    if (item == existingItem) {
                        item.copy(cantidad = item.cantidad + 1)
                    } else {
                        item
                    }
                }
            } else {
                currentState.cartItems + CartItem(
                    product = product,
                    cantidad = 1
                )
            }

            currentState.copy(cartItems = updatedItems)
        }
    }

    private fun removeFromCart(item: CartItem) {
        _state.update { currentState ->
            currentState.copy(
                cartItems = currentState.cartItems.filter { it != item }
            )
        }
    }

    private fun updateQuantity(item: CartItem, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(item)
        } else {
            _state.update { currentState ->
                currentState.copy(
                    cartItems = currentState.cartItems.map {
                        if (it == item) it.copy(cantidad = newQuantity) else it
                    }
                )
            }
        }
    }

    private fun confirmOrder(usuarioId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Preparar lista de productos con cantidades
                val productos = _state.value.cartItems.map { cartItem ->
                    Pair(cartItem.product.id, cartItem.cantidad)
                }

                // Crear pedido en el backend
                val order = orderRepository.createOrder(usuarioId, productos)

                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        confirmedOrder = order,
                        cartItems = emptyList() // Limpiar carrito
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al confirmar pedido"
                    )
                }
            }
        }
    }

    private fun clearCart() {
        _state.update {
            it.copy(
                cartItems = emptyList(),
                confirmedOrder = null,
                error = null
            )
        }
    }

    fun getCartItemCount(): Int = _state.value.cartItems.size
}