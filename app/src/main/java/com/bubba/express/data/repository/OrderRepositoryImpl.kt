package com.bubba.express.data.repository

import com.bubba.express.data.remote.api.BubbaApi
import com.bubba.express.data.remote.dto.CrearPedidoProductoDto
import com.bubba.express.data.remote.dto.CrearPedidoRequestDto
import com.bubba.express.domain.model.Order
import com.bubba.express.domain.model.OrderItem
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: BubbaApi
) : OrderRepository {

    override suspend fun getOrders(): List<Order> {
        return api.getPedidos().map {
            Order(
                id = it.id_pedido,
                usuarioId = it.id_usuario,
                total = it.total,
                estado = it.estado,
                items = emptyList()
            )
        }
    }

    override suspend fun getOrderById(id: Int): Order {
        val dto = api.getPedidoPorId(id)
        return Order(
            id = dto.id_pedido,
            usuarioId = dto.id_usuario,
            total = dto.total,
            estado = dto.estado,
            items = dto.detalles.map { det ->
                OrderItem(
                    id = det.id_detalle_pedido,
                    nombreProducto = det.nombre_producto,
                    cantidad = det.cantidad,
                    precioUnitario = det.precio_unitario,
                    subtotal = det.subtotal
                )
            }
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

        val dto = api.crearPedidoCompleto(request)

        return Order(
            id = dto.id_pedido,
            usuarioId = dto.id_usuario,
            total = dto.total,
            estado = dto.estado,
            items = dto.detalles.map {
                OrderItem(
                    id = it.id_detalle_pedido,
                    nombreProducto = it.nombre_producto,
                    cantidad = it.cantidad,
                    precioUnitario = it.precio_unitario,
                    subtotal = it.subtotal
                )
            }
        )
    }
}