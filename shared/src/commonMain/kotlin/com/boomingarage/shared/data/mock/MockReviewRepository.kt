package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.Review
import com.boomingarage.shared.domain.repository.ReviewRepository
import com.boomingarage.shared.util.DateTimeUtil

class MockReviewRepository : ReviewRepository {
    private val reviews = SampleData.reviews.toMutableList()

    override suspend fun submit(review: Review): Result<Review> {
        val created = review.copy(
            id = "rev-${reviews.size + 1}",
            createdAt = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        )
        reviews.add(created)
        return Result.success(created)
    }

    override suspend fun getForGarage(garageId: String): Result<List<Review>> {
        return Result.success(
            reviews.filter { it.garageId == garageId }.sortedByDescending { it.createdAt }
        )
    }

    override suspend fun getByUser(userId: String): Result<List<Review>> {
        return Result.success(reviews.filter { it.userId == userId })
    }
}
