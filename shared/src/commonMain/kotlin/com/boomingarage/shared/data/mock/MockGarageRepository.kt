package com.boomingarage.shared.data.mock

import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.model.TimeSlot
import com.boomingarage.shared.domain.repository.GarageRepository
import com.boomingarage.shared.util.DateTimeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.*

class MockGarageRepository : GarageRepository {
    private val garages = SampleData.garages.toMutableList()
    private val bayFlows = mutableMapOf<String, MutableStateFlow<List<Bay>>>()

    override suspend fun searchNearby(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): Result<List<Garage>> {
        val filtered = garages.filter { garage ->
            haversine(latitude, longitude, garage.latitude, garage.longitude) <= radiusKm
        }
        return Result.success(filtered)
    }

    override suspend fun getById(garageId: String): Result<Garage> {
        val garage = garages.find { it.id == garageId }
            ?: return Result.failure(Exception("Garage not found"))
        return Result.success(garage)
    }

    override suspend fun getBays(garageId: String): Result<List<Bay>> {
        return Result.success(SampleData.baysForGarage(garageId))
    }

    override suspend fun getAvailableTimeSlots(
        garageId: String,
        bayId: String,
        date: String
    ): Result<List<TimeSlot>> {
        val garage = garages.find { it.id == garageId }
            ?: return Result.failure(Exception("Garage not found"))

        val dayOfWeek = kotlinx.datetime.LocalDate.parse(date).dayOfWeek.name
        val hours = garage.operatingHours[dayOfWeek]
        if (hours == null || hours.isClosed) return Result.success(emptyList())

        val allSlots = DateTimeUtil.generateTimeSlots(hours.openTime, hours.closeTime)

        // Simulate some slots being booked
        val bookedSlots = setOf("10:00", "11:00", "14:00") // simulate bookings

        val slots = allSlots.map { slot ->
            val endMinutes = slot.split(":").let { it[0].toInt() * 60 + it[1].toInt() } + 60
            val endTime = "${(endMinutes / 60).toString().padStart(2, '0')}:${(endMinutes % 60).toString().padStart(2, '0')}"
            TimeSlot(
                startTime = slot,
                endTime = endTime,
                isAvailable = slot !in bookedSlots
            )
        }
        return Result.success(slots)
    }

    override fun observeBayAvailability(garageId: String): Flow<List<Bay>> {
        return bayFlows.getOrPut(garageId) {
            MutableStateFlow(SampleData.baysForGarage(garageId))
        }
    }

    override suspend fun updateBayAvailability(
        garageId: String,
        bayId: String,
        isAvailable: Boolean
    ): Result<Unit> {
        val flow = bayFlows.getOrPut(garageId) {
            MutableStateFlow(SampleData.baysForGarage(garageId))
        }
        flow.value = flow.value.map {
            if (it.id == bayId) it.copy(isAvailable = isAvailable) else it
        }
        return Result.success(Unit)
    }

    override suspend fun getGaragesByOperator(operatorId: String): Result<List<Garage>> {
        return Result.success(garages.filter { it.operatorId == operatorId })
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }
}
