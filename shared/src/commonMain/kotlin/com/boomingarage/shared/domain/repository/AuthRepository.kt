package com.boomingarage.shared.domain.repository

import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.model.CarInfo
import com.boomingarage.shared.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signUp(email: String, password: String, displayName: String, phone: String, role: UserRole): Result<User>
    suspend fun signOut()
    suspend fun updateProfile(displayName: String? = null, phone: String? = null, carInfo: CarInfo? = null): Result<User>
    suspend fun sendEmailVerification(): Result<Unit>
    suspend fun getCurrentUser(): User?
}
