package com.boomingarage.shared.domain.usecase.garage

import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.TimeSlot
import com.boomingarage.shared.domain.repository.GarageRepository

data class BayAvailability(
    val bay: Bay,
    val timeSlots: List<TimeSlot>
)

class GetAvailableBaysUseCase(private val garageRepository: GarageRepository) {
    suspend operator fun invoke(garageId: String, date: String): Result<List<BayAvailability>> {
        val bays = garageRepository.getBays(garageId).getOrElse { return Result.failure(it) }
        val availableBays = bays.filter { it.isAvailable }

        val result = availableBays.map { bay ->
            val slots = garageRepository.getAvailableTimeSlots(garageId, bay.id, date)
                .getOrDefault(emptyList())
            BayAvailability(bay, slots)
        }
        return Result.success(result)
    }
}
