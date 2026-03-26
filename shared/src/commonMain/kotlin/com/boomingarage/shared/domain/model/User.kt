package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val displayName: String,
    val phone: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val profileImageUrl: String? = null,
    val carInfo: CarInfo? = null,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val createdAt: Long = 0L
)

@Serializable
enum class UserRole {
    CUSTOMER,
    OPERATOR
}

@Serializable
data class CarInfo(
    val make: String,
    val model: String,
    val year: Int,
    val color: String = "",
    val licensePlate: String = ""
)
