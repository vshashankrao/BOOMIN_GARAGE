package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Bay(
    val id: String,
    val garageId: String,
    val name: String,
    val number: Int,
    val isAvailable: Boolean = true,
    val features: List<String> = emptyList(),
    val hourlyRate: Double? = null // null means use garage default rate
)
