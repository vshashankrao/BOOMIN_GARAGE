package com.boomingarage.shared.data.repository

import com.boomingarage.shared.data.mapper.ReviewMapper
import com.boomingarage.shared.data.remote.dto.ReviewDto
import com.boomingarage.shared.domain.model.Review
import com.boomingarage.shared.domain.repository.ReviewRepository
import com.boomingarage.shared.util.Constants
import com.boomingarage.shared.util.DateTimeUtil
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class ReviewRepositoryImpl : ReviewRepository {
    private val firestore = Firebase.firestore
    private val collection = firestore.collection(Constants.COLLECTION_REVIEWS)

    override suspend fun submit(review: Review): Result<Review> = runCatching {
        val dto = ReviewMapper.toDto(review).copy(
            createdAt = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        )
        val docRef = collection.add(dto)

        // Update garage average rating
        val garageRef = firestore.collection(Constants.COLLECTION_GARAGES).document(review.garageId)
        val garageDoc = garageRef.get()
        val currentTotal = garageDoc.get<Int>("totalReviews")
        val currentAvg = garageDoc.get<Double>("averageRating")
        val newTotal = currentTotal + 1
        val newAvg = ((currentAvg * currentTotal) + review.rating) / newTotal
        garageRef.update(mapOf("totalReviews" to newTotal, "averageRating" to newAvg))

        ReviewMapper.toDomain(dto.copy(id = docRef.id))
    }

    override suspend fun getForGarage(garageId: String): Result<List<Review>> = runCatching {
        val snapshot = collection.where { "garageId" equalTo garageId }.get()
        snapshot.documents.map { doc ->
            ReviewMapper.toDomain(doc.data<ReviewDto>().copy(id = doc.id))
        }.sortedByDescending { it.createdAt }
    }

    override suspend fun getByUser(userId: String): Result<List<Review>> = runCatching {
        val snapshot = collection.where { "userId" equalTo userId }.get()
        snapshot.documents.map { doc ->
            ReviewMapper.toDomain(doc.data<ReviewDto>().copy(id = doc.id))
        }
    }
}
