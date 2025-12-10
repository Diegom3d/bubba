package com.bubba.express.data.remote.dto

data class PedidoDetalleDto(
    val id_producto: Int,
    val cantidad: Int,
    val precio: Double,
    val subtotal: Double
)
