package com.bubba.express.data.repository

import com.bubba.express.data.remote.api.BubbaApi
import com.bubba.express.domain.model.Product
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: BubbaApi
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return api.getProductos().map {
            Product(
                id = it.id_producto,
                nombre = it.nombre,
                descripcion = it.descripcion,
                precio = it.precio,
                disponible = it.disponible == 1,
                categoria = it.categoria
            )
        }
    }
}