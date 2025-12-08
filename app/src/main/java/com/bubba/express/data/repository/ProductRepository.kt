package com.bubba.express.data.repository

import com.bubba.express.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}