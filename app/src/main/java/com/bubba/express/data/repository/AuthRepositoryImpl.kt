package com.bubba.express.data.repository

import com.bubba.express.data.remote.api.BubbaApi
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
}