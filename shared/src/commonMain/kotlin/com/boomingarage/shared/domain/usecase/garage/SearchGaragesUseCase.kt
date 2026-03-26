package com.boomingarage.shared.domain.usecase.garage

import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.repository.GarageRepository

class SearchGaragesUseCase(private val garageRepository: GarageRepository) {
    suspend operator fun invoke(latitude: Double, longitude: Double, radiusKm: Double = 25.0): Result<List<Garage>> {
        return garageRepository.searchNearby(latitude, longitude, radiusKm)
    }
}
