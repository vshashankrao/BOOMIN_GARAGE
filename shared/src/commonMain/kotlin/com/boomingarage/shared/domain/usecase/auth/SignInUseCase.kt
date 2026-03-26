package com.boomingarage.shared.domain.usecase.auth

import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.repository.AuthRepository

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email is required"))
        if (password.isBlank()) return Result.failure(IllegalArgumentException("Password is required"))
        return authRepository.signIn(email.trim(), password)
    }
}
