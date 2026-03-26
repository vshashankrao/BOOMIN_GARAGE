package com.boomingarage.shared.domain.repository

import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.model.TimeSlot
import kotlinx.coroutines.flow.Flow

interface GarageRepository {
    suspend fun searchNearby(latitude: Double, longitude: Double, radiusKm: Double = 25.0): Result<List<Garage>>
    suspend fun getById(garageId: String): Result<Garage>
    suspend fun getBays(garageId: String): Result<List<Bay>>
    suspend fun getAvailableTimeSlots(garageId: String, bayId: String, date: String): Result<List<TimeSlot>>
    fun observeBayAvailability(garageId: String): Flow<List<Bay>>
    suspend fun updateBayAvailability(garageId: String, bayId: String, isAvailable: Boolean): Result<Unit>
    suspend fun getGaragesByOperator(operatorId: String): Result<List<Garage>>
}
