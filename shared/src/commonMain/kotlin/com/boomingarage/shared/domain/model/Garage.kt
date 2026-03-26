package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Garage(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val operatorId: String,
    val photoUrls: List<String> = emptyList(),
    val amenities: List<String> = emptyList(),
    val hourlyRate: Double,
    val operatingHours: Map<String, DayHours> = emptyMap(),
    val bayCount: Int = 0,
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val description: String = ""
)

@Serializable
data class DayHours(
    val openTime: String,  // "08:00"
    val closeTime: String, // "20:00"
    val isClosed: Boolean = false
)
