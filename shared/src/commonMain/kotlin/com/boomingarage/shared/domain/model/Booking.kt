package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val id: String,
    val garageId: String,
    val garageName: String = "",
    val bayId: String,
    val bayName: String = "",
    val userId: String,
    val date: String, // "2026-03-25"
    val startTime: String, // "10:00"
    val endTime: String, // "13:00"
    val hoursBooked: Int,
    val hourlyRate: Double,
    val status: BookingStatus = BookingStatus.PENDING,
    val depositAmount: Double = 0.0,
    val totalEstimate: Double = 0.0,
    val actualTotal: Double? = null,
    val createdAt: Long = 0L
)

@Serializable
enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CHECKED_IN,
    COMPLETED,
    CANCELLED
}
