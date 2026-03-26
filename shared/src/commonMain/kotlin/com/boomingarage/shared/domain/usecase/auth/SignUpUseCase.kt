package com.boomingarage.shared.domain.usecase.auth

import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.model.UserRole
import com.boomingarage.shared.domain.repository.AuthRepository

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String,
        phone: String,
        role: UserRole = UserRole.CUSTOMER
    ): Result<User> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email is required"))
        if (password.length < 6) return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        if (displayName.isBlank()) return Result.failure(IllegalArgumentException("Name is required"))
        return authRepository.signUp(email.trim(), password, displayName.trim(), phone.trim(), role)
    }
}
