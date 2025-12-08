package com.bubba.express.domain.model

data class Product(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val disponible: Boolean,
    val categoria: String?
)