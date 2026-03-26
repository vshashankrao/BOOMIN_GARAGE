package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val startTime: String, // "08:00"
    val endTime: String,   // "09:00"
    val isAvailable: Boolean = true
)
