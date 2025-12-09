package com.bubba.express.data.repository

import com.bubba.express.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(nombre: String, email: String, password: String): Result<String>
    suspend fun getUsers(): List<User>
}
