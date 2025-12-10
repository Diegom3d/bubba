package com.bubba.express.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bubba.express.data.repository.ProductRepository
import com.bubba.express.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadProducts()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadProducts -> loadProducts()
            is HomeEvent.OnProductClick -> {
                // La navegación se maneja en el composable
            }
            is HomeEvent.OnCartClick -> {
                // La navegación se maneja en el composable
            }
            is HomeEvent.OnProfileClick -> {
                // La navegación se maneja en el composable
            }
            is HomeEvent.OnLogoutClick -> {
                // TODO: Implementar logout
            }
            is HomeEvent.OnHistoryClick -> {
                // La navegación se maneja en el composable
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val products = productRepository.getProducts()
                _state.update {
                    it.copy(
                        isLoading = false,
                        products = products
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar productos"
                    )
                }
            }
        }
    }

    fun setCurrentUser(user: User) {
        _state.update { it.copy(currentUser = user) }
    }

    fun updateCartCount(count: Int) {
        _state.update { it.copy(cartItemCount = count) }
    }
}