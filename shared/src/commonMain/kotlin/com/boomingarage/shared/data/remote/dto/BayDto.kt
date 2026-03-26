package com.boomingarage.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BayDto(
    val id: String = "",
    val garageId: String = "",
    val name: String = "",
    val number: Int = 0,
    val isAvailable: Boolean = true,
    val features: List<String> = emptyList(),
    val hourlyRate: Double? = null
)
