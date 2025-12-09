package com.bubba.express.data.remote.api

import com.bubba.express.data.remote.dto.CrearPedidoRequestDto
import com.bubba.express.data.remote.dto.CrearPedidoResponseDto
import com.bubba.express.data.remote.dto.PedidoDetalleDto
import com.bubba.express.data.remote.dto.PedidoDto
import com.bubba.express.data.remote.dto.ProductoDto
import com.bubba.express.data.remote.dto.UsuarioDto
import com.bubba.express.data.remote.dto.LoginRequestDto
import com.bubba.express.data.remote.dto.LoginResponseDto
import com.bubba.express.data.remote.dto.RegisterRequestDto
import com.bubba.express.data.remote.dto.RegisterResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BubbaApi {

    @GET("v1/usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @GET("v1/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("v1/pedidos")
    suspend fun getPedidos(): List<PedidoDto>

    @GET("v1/pedidos/pedido/{id_pedido}")
    suspend fun getPedidoPorId(
        @Path("id_pedido") idPedido: Int
    ): PedidoDetalleDto

    @POST("v1/pedidos/completo")
    suspend fun crearPedidoCompleto(
        @Body request: CrearPedidoRequestDto
    ): CrearPedidoResponseDto

    @POST("v1/usuarios/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @POST("v1/usuarios")
    suspend fun registerUser(
        @Body request: RegisterRequestDto
    ): RegisterResponseDto

}