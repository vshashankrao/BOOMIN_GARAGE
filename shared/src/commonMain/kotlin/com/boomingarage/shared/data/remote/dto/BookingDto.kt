package com.boomingarage.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val id: String = "",
    val garageId: String = "",
    val garageName: String = "",
    val bayId: String = "",
    val bayName: String = "",
    val userId: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val hoursBooked: Int = 0,
    val hourlyRate: Double = 0.0,
    val status: String = "PENDING",
    val depositAmount: Double = 0.0,
    val totalEstimate: Double = 0.0,
    val actualTotal: Double? = null,
    val createdAt: Long = 0L
)
