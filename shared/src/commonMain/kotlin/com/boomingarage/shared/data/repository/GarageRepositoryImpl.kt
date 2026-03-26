package com.boomingarage.shared.data.repository

import com.boomingarage.shared.data.mapper.GarageMapper
import com.boomingarage.shared.data.remote.dto.BayDto
import com.boomingarage.shared.data.remote.dto.GarageDto
import com.boomingarage.shared.domain.model.Bay
import com.boomingarage.shared.domain.model.Garage
import com.boomingarage.shared.domain.model.TimeSlot
import com.boomingarage.shared.domain.repository.GarageRepository
import com.boomingarage.shared.util.Constants
import com.boomingarage.shared.util.DateTimeUtil
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GarageRepositoryImpl : GarageRepository {
    private val firestore = Firebase.firestore

    override suspend fun searchNearby(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): Result<List<Garage>> = runCatching {
        // For MVP, fetch all garages and filter client-side by distance
        // In production, use GeoFirestore or server-side geoqueries
        val snapshot = firestore.collection(Constants.COLLECTION_GARAGES).get()
        snapshot.documents.map { doc ->
            val dto = doc.data<GarageDto>()
            GarageMapper.toDomain(dto.copy(id = doc.id))
        }.filter { garage ->
            calculateDistance(latitude, longitude, garage.latitude, garage.longitude) <= radiusKm
        }
    }

    override suspend fun getById(garageId: String): Result<Garage> = runCatching {
        val doc = firestore.collection(Constants.COLLECTION_GARAGES).document(garageId).get()
        val dto = doc.data<GarageDto>()
        GarageMapper.toDomain(dto.copy(id = doc.id))
    }

    override suspend fun getBays(garageId: String): Result<List<Bay>> = runCatching {
        val snapshot = firestore.collection(Constants.COLLECTION_GARAGES)
            .document(garageId)
            .collection(Constants.COLLECTION_BAYS)
            .get()
        snapshot.documents.map { doc ->
            val dto = doc.data<BayDto>()
            GarageMapper.bayToDomain(dto.copy(id = doc.id, garageId = garageId))
        }
    }

    override suspend fun getAvailableTimeSlots(
        garageId: String,
        bayId: String,
        date: String
    ): Result<List<TimeSlot>> = runCatching {
        val garage = getById(garageId).getOrThrow()
        val dayOfWeek = kotlinx.datetime.LocalDate.parse(date).dayOfWeek.name
        val hours = garage.operatingHours[dayOfWeek]

        if (hours == null || hours.isClosed) return@runCatching emptyList()

        val allSlots = DateTimeUtil.generateTimeSlots(hours.openTime, hours.closeTime)

        // Check existing bookings for this bay on this date
        val bookings = firestore.collection(Constants.COLLECTION_BOOKINGS)
            .where { "bayId" equalTo bayId }
            .where { "date" equalTo date }
            .get()

        val bookedSlots = bookings.documents.flatMap { doc ->
            val startTime = doc.get<String>("startTime")
            val endTime = doc.get<String>("endTime")
            DateTimeUtil.generateTimeSlots(startTime, endTime)
        }.toSet()

        allSlots.map { slot ->
            val endMinutes = slot.split(":").let { it[0].toInt() * 60 + it[1].toInt() } + Constants.TIME_SLOT_DURATION_MINUTES
            val endTime = "${(endMinutes / 60).toString().padStart(2, '0')}:${(endMinutes % 60).toString().padStart(2, '0')}"
            TimeSlot(
                startTime = slot,
                endTime = endTime,
                isAvailable = slot !in bookedSlots
            )
        }
    }

    override fun observeBayAvailability(garageId: String): Flow<List<Bay>> {
        return firestore.collection(Constants.COLLECTION_GARAGES)
            .document(garageId)
            .collection(Constants.COLLECTION_BAYS)
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { doc ->
                    val dto = doc.data<BayDto>()
                    GarageMapper.bayToDomain(dto.copy(id = doc.id, garageId = garageId))
                }
            }
    }

    override suspend fun updateBayAvailability(
        garageId: String,
        bayId: String,
        isAvailable: Boolean
    ): Result<Unit> = runCatching {
        firestore.collection(Constants.COLLECTION_GARAGES)
            .document(garageId)
            .collection(Constants.COLLECTION_BAYS)
            .document(bayId)
            .update(mapOf("isAvailable" to isAvailable))
    }

    override suspend fun getGaragesByOperator(operatorId: String): Result<List<Garage>> = runCatching {
        val snapshot = firestore.collection(Constants.COLLECTION_GARAGES)
            .where { "operatorId" equalTo operatorId }
            .get()
        snapshot.documents.map { doc ->
            val dto = doc.data<GarageDto>()
            GarageMapper.toDomain(dto.copy(id = doc.id))
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return r * c
    }
}
