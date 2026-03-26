package com.boomingarage.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GarageDto(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val operatorId: String = "",
    val photoUrls: List<String> = emptyList(),
    val amenities: List<String> = emptyList(),
    val hourlyRate: Double = 0.0,
    val operatingHours: Map<String, DayHoursDto> = emptyMap(),
    val bayCount: Int = 0,
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val description: String = ""
)

@Serializable
data class DayHoursDto(
    val openTime: String = "08:00",
    val closeTime: String = "20:00",
    val isClosed: Boolean = false
)
