package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.CarInfo
import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.model.UserRole
import com.boomingarage.shared.domain.repository.AuthRepository
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(null)

    override val currentUser: Flow<User?> = _currentUser

    override suspend fun signIn(email: String, password: String): Result<User> {
        val user = if (email.contains("operator", ignoreCase = true)) {
            SampleData.operator
        } else {
            SampleData.customer
        }
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        displayName: String,
        phone: String,
        role: UserRole
    ): Result<User> {
        val user = User(
            id = "user-${DateTimeUtil.toEpochMillis(DateTimeUtil.now())}",
            email = email,
            displayName = displayName,
            phone = phone,
            role = role,
            isEmailVerified = false,
            createdAt = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun signOut() {
        _currentUser.value = null
    }

    override suspend fun updateProfile(
        displayName: String?,
        phone: String?,
        carInfo: CarInfo?
    ): Result<User> {
        val current = _currentUser.value ?: return Result.failure(Exception("Not signed in"))
        val updated = current.copy(
            displayName = displayName ?: current.displayName,
            phone = phone ?: current.phone,
            carInfo = carInfo ?: current.carInfo
        )
        _currentUser.value = updated
        return Result.success(updated)
    }

    override suspend fun sendEmailVerification(): Result<Unit> = Result.success(Unit)

    override suspend fun getCurrentUser(): User? = _currentUser.value
}
