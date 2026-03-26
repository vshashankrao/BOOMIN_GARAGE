package com.boomingarage.shared.domain.repository

import com.boomingarage.shared.domain.model.Review

interface ReviewRepository {
    suspend fun submit(review: Review): Result<Review>
    suspend fun getForGarage(garageId: String): Result<List<Review>>
    suspend fun getByUser(userId: String): Result<List<Review>>
}
