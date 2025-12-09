package com.bubba.express.domain.usecase

import com.bubba.express.data.repository.AuthRepository
import com.bubba.express.domain.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): User? {
        val users = authRepository.getUsers()
        return users.firstOrNull { it.email == email }
    }
}
