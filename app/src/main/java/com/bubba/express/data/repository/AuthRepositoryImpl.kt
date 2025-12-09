package com.bubba.express.data.repository

import com.bubba.express.data.remote.api.BubbaApi
import com.bubba.express.data.remote.dto.LoginRequestDto
import com.bubba.express.data.remote.dto.RegisterRequestDto
import com.bubba.express.data.remote.dto.RegisterResponseDto
import com.bubba.express.domain.model.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: BubbaApi
) : AuthRepository {

    override suspend fun getUsers(): List<User> {
        return api.getUsuarios().map {
            User(
                id = it.id_usuario,
                nombre = it.nombre,
                email = it.email
            )
        }
    }

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(
                LoginRequestDto(
                    email = email,
                    contrasenia = password
                )
            )
            Result.success(response.email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(nombre: String, email: String, password: String): Result<String> {
        return try {
            val response = api.registerUser(
                RegisterRequestDto(
                    nombre = nombre,
                    email = email,
                    contrasenia = password
                )
            )
            Result.success(response.data.email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
