package com.boomingarage.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val id: String = "",
    val garageId: String = "",
    val userId: String = "",
    val userName: String = "",
    val bookingId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val createdAt: Long = 0L
)
