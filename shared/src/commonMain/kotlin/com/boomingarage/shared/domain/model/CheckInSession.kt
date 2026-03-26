package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckInSession(
    val id: String,
    val bookingId: String,
    val userId: String,
    val garageId: String,
    val bayId: String,
    val startedAt: Long,
    val scheduledEndAt: Long,
    val actualEndAt: Long? = null,
    val isOvertime: Boolean = false,
    val overtimeMinutes: Int = 0
)
