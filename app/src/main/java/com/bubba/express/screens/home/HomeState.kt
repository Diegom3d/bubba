package com.bubba.express.screens.home

import com.bubba.express.domain.model.Product
import com.bubba.express.domain.model.User

data class HomeState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val currentUser: User? = null,
    val cartItemCount: Int = 0,
    val error: String? = null
)