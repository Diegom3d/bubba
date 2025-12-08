package com.bubba.express.data.repository

import com.bubba.express.domain.model.User

interface AuthRepository {
    suspend fun getUsers(): List<User>
}