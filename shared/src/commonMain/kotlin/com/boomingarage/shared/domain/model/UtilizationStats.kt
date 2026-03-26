package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UtilizationStats(
    val garageId: String,
    val date: String,
    val totalBays: Int,
    val bookingsToday: Int,
    val revenueToday: Double,
    val utilizationPercent: Double,
    val weeklyRevenue: Double = 0.0,
    val weeklyBookings: Int = 0,
    val averageSessionHours: Double = 0.0
)
