package com.bubba.express.data.repository

import com.bubba.express.data.remote.api.BubbaApi
import com.bubba.express.data.remote.dto.CrearPedidoProductoDto
import com.bubba.express.data.remote.dto.CrearPedidoRequestDto
import com.bubba.express.data.remote.dto.CrearPedidoResponseDto
import com.bubba.express.data.remote.dto.PedidoCreadoDataDto
import com.bubba.express.data.remote.dto.PedidoDetalleDto
import com.bubba.express.data.remote.dto.PedidoDto
import com.bubba.express.domain.model.Order
import com.bubba.express.domain.model.OrderItem
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: BubbaApi
) : OrderRepository {

    override suspend fun getOrders(): List<Order> {
        val list = api.getPedidos()
        return list.map { dto ->
            Order(
                id = dto.id_pedido,
                usuarioId = dto.id_usuario,
                total = dto.total,
                estado = dto.estado,
                items = emptyList()
            )
        }
    }

    override suspend fun getOrderById(id: Int): Order {
        val dto: PedidoDto = api.getPedidoPorId(id)

        return Order(
            id = dto.id_pedido,
            usuarioId = dto.id_usuario,
            total = dto.total,
            estado = dto.estado,
            items = emptyList() // tu backend NO devuelve detalles aquí
        )
    }

    override suspend fun createOrder(
        usuarioId: Int,
        productos: List<Pair<Int, Int>>
    ): Order {

        val request = CrearPedidoRequestDto(
            id_usuario = usuarioId,
            productos = productos.map {
                CrearPedidoProductoDto(
                    id_producto = it.first,
                    cantidad = it.second
                )
            }
        )

        val response: CrearPedidoResponseDto = api.crearPedidoCompleto(request)
        val data: PedidoCreadoDataDto = response.data

        return Order(
            id = data.id_pedido,
            usuarioId = data.id_usuario,
            total = data.total,
            estado = data.estado,
            items = data.detalles.map { det: PedidoDetalleDto ->
                OrderItem(
                    id = det.id_producto,
                    nombreProducto = "Producto #${det.id_producto}", // por ahora, backend no manda nombre
                    cantidad = det.cantidad,
                    precioUnitario = det.precio,
                    subtotal = det.subtotal
                )
            }
        )
    }
}
