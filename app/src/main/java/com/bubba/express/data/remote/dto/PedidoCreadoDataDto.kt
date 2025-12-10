package com.bubba.express.data.remote.dto

data class PedidoCreadoDataDto(
    val id_pedido: Int,
    val id_usuario: Int,
    val total: Double,
    val estado: String,
    val detalles: List<PedidoDetalleDto>
)
