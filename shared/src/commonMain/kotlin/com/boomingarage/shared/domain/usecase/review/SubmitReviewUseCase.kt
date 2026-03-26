package com.boomingarage.shared.domain.usecase.review

import com.boomingarage.shared.domain.model.Review
import com.boomingarage.shared.domain.repository.ReviewRepository

class SubmitReviewUseCase(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(review: Review): Result<Review> {
        if (review.rating !in 1..5) return Result.failure(IllegalArgumentException("Rating must be between 1 and 5"))
        return reviewRepository.submit(review)
    }
}
