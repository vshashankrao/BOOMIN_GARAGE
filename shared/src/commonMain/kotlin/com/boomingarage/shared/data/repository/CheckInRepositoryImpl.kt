package com.boomingarage.shared.data.repository

import com.boomingarage.shared.domain.model.CheckInSession
import com.boomingarage.shared.domain.repository.CheckInRepository
import com.boomingarage.shared.util.Constants
import com.boomingarage.shared.util.DateTimeUtil
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
private data class CheckInDto(
    val id: String = "",
    val bookingId: String = "",
    val userId: String = "",
    val garageId: String = "",
    val bayId: String = "",
    val startedAt: Long = 0L,
    val scheduledEndAt: Long = 0L,
    val actualEndAt: Long? = null,
    val isOvertime: Boolean = false,
    val overtimeMinutes: Int = 0
)

class CheckInRepositoryImpl : CheckInRepository {
    private val firestore = Firebase.firestore
    private val collection = firestore.collection(Constants.COLLECTION_CHECKINS)

    override suspend fun startSession(bookingId: String): Result<CheckInSession> = runCatching {
        val bookingDoc = firestore.collection(Constants.COLLECTION_BOOKINGS).document(bookingId).get()
        val userId = bookingDoc.get<String>("userId")
        val garageId = bookingDoc.get<String>("garageId")
        val bayId = bookingDoc.get<String>("bayId")
        val hoursBooked = bookingDoc.get<Int>("hoursBooked")

        val now = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        val scheduledEnd = now + (hoursBooked * 3600 * 1000L)

        val dto = CheckInDto(
            bookingId = bookingId,
            userId = userId,
            garageId = garageId,
            bayId = bayId,
            startedAt = now,
            scheduledEndAt = scheduledEnd
        )

        val docRef = collection.add(dto)
        CheckInSession(
            id = docRef.id,
            bookingId = bookingId,
            userId = userId,
            garageId = garageId,
            bayId = bayId,
            startedAt = now,
            scheduledEndAt = scheduledEnd
        )
    }

    override suspend fun endSession(sessionId: String): Result<CheckInSession> = runCatching {
        val now = DateTimeUtil.toEpochMillis(DateTimeUtil.now())
        val doc = collection.document(sessionId)
        doc.update(mapOf("actualEndAt" to now))

        val updated = doc.get()
        val dto = updated.data<CheckInDto>()
        toDomain(dto.copy(id = sessionId, actualEndAt = now))
    }

    override suspend fun extendSession(sessionId: String, additionalMinutes: Int): Result<CheckInSession> = runCatching {
        val doc = collection.document(sessionId).get()
        val dto = doc.data<CheckInDto>()
        val newEnd = dto.scheduledEndAt + (additionalMinutes * 60 * 1000L)

        collection.document(sessionId).update(mapOf("scheduledEndAt" to newEnd))
        toDomain(dto.copy(id = sessionId, scheduledEndAt = newEnd))
    }

    override fun observeActiveSession(userId: String): Flow<CheckInSession?> {
        return collection
            .where { "userId" equalTo userId }
            .where { "actualEndAt" equalTo null }
            .snapshots
            .map { snapshot ->
                snapshot.documents.firstOrNull()?.let { doc ->
                    toDomain(doc.data<CheckInDto>().copy(id = doc.id))
                }
            }
    }

    override suspend fun getActiveSession(userId: String): Result<CheckInSession?> = runCatching {
        val snapshot = collection
            .where { "userId" equalTo userId }
            .where { "actualEndAt" equalTo null }
            .get()
        snapshot.documents.firstOrNull()?.let { doc ->
            toDomain(doc.data<CheckInDto>().copy(id = doc.id))
        }
    }

    override suspend fun requestAssistance(sessionId: String, message: String): Result<Unit> = runCatching {
        // Store assistance request as a subcollection or separate collection
        firestore.collection("assistance_requests").add(
            mapOf(
                "sessionId" to sessionId,
                "message" to message,
                "timestamp" to DateTimeUtil.toEpochMillis(DateTimeUtil.now()),
                "status" to "PENDING"
            )
        )
    }

    private fun toDomain(dto: CheckInDto) = CheckInSession(
        id = dto.id,
        bookingId = dto.bookingId,
        userId = dto.userId,
        garageId = dto.garageId,
        bayId = dto.bayId,
        startedAt = dto.startedAt,
        scheduledEndAt = dto.scheduledEndAt,
        actualEndAt = dto.actualEndAt,
        isOvertime = dto.isOvertime,
        overtimeMinutes = dto.overtimeMinutes
    )
}
