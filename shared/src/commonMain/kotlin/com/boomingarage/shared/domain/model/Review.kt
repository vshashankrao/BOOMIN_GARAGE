package com.boomingarage.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String,
    val garageId: String,
    val userId: String,
    val userName: String = "",
    val bookingId: String,
    val rating: Int, // 1-5
    val comment: String = "",
    val createdAt: Long = 0L
)
