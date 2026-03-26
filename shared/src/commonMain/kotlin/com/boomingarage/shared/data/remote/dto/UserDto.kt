package com.boomingarage.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val phone: String = "",
    val role: String = "CUSTOMER",
    val profileImageUrl: String? = null,
    val carMake: String? = null,
    val carModel: String? = null,
    val carYear: Int? = null,
    val carColor: String? = null,
    val carLicensePlate: String? = null,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val createdAt: Long = 0L
)
