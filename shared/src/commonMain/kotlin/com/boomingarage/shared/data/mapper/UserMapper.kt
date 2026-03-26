package com.boomingarage.shared.data.mapper

import com.boomingarage.shared.data.remote.dto.UserDto
import com.boomingarage.shared.domain.model.CarInfo
import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.model.UserRole

object UserMapper {
    fun toDomain(dto: UserDto): User = User(
        id = dto.id,
        email = dto.email,
        displayName = dto.displayName,
        phone = dto.phone,
        role = try { UserRole.valueOf(dto.role) } catch (_: Exception) { UserRole.CUSTOMER },
        profileImageUrl = dto.profileImageUrl,
        carInfo = if (dto.carMake != null && dto.carModel != null && dto.carYear != null) {
            CarInfo(
                make = dto.carMake,
                model = dto.carModel,
                year = dto.carYear,
                color = dto.carColor ?: "",
                licensePlate = dto.carLicensePlate ?: ""
            )
        } else null,
        isEmailVerified = dto.isEmailVerified,
        isPhoneVerified = dto.isPhoneVerified,
        createdAt = dto.createdAt
    )

    fun toDto(user: User): UserDto = UserDto(
        id = user.id,
        email = user.email,
        displayName = user.displayName,
        phone = user.phone,
        role = user.role.name,
        profileImageUrl = user.profileImageUrl,
        carMake = user.carInfo?.make,
        carModel = user.carInfo?.model,
        carYear = user.carInfo?.year,
        carColor = user.carInfo?.color,
        carLicensePlate = user.carInfo?.licensePlate,
        isEmailVerified = user.isEmailVerified,
        isPhoneVerified = user.isPhoneVerified,
        createdAt = user.createdAt
    )
}
