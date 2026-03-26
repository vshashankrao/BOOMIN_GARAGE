package com.boomingarage.shared.data.mapper

import com.boomingarage.shared.data.remote.dto.ReviewDto
import com.boomingarage.shared.domain.model.Review

object ReviewMapper {
    fun toDomain(dto: ReviewDto): Review = Review(
        id = dto.id,
        garageId = dto.garageId,
        userId = dto.userId,
        userName = dto.userName,
        bookingId = dto.bookingId,
        rating = dto.rating,
        comment = dto.comment,
        createdAt = dto.createdAt
    )

    fun toDto(review: Review): ReviewDto = ReviewDto(
        id = review.id,
        garageId = review.garageId,
        userId = review.userId,
        userName = review.userName,
        bookingId = review.bookingId,
        rating = review.rating,
        comment = review.comment,
        createdAt = review.createdAt
    )
}
