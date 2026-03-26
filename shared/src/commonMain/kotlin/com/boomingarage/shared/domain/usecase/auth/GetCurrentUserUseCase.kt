package com.boomingarage.shared.domain.usecase.auth

import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<User?> = authRepository.currentUser
    suspend fun getOnce(): User? = authRepository.getCurrentUser()
}
