package com.boomingarage.shared.domain.usecase.garage

import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.model.Review
import com.boomingarage.shared.domain.repository.GarageRepository
import com.boomingarage.shared.domain.repository.ReviewRepository

data class GarageDetails(
    val garage: Garage,
    val bays: List<Bay>,
    val reviews: List<Review>
)

class GetGarageDetailsUseCase(
    private val garageRepository: GarageRepository,
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(garageId: String): Result<GarageDetails> {
        val garage = garageRepository.getById(garageId).getOrElse { return Result.failure(it) }
        val bays = garageRepository.getBays(garageId).getOrDefault(emptyList())
        val reviews = reviewRepository.getForGarage(garageId).getOrDefault(emptyList())
        return Result.success(GarageDetails(garage, bays, reviews))
    }
}
